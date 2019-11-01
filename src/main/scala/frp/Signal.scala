package frp
import scala.util.DynamicVariable


// Each signal maintains 
// - its current time
// - the current expression that defines the signal value
// - a set of observers: the other signals that depend on its value 

// If the signal changes, all observers must be updated

class Signal[T](expr: => T) {
    import Signal._
    private var myExp: () => T = _ 
    private var myValue: T = _ 
    private var observers: Set[Signal[_]] = Set()
   
    def apply(): T = {
        observers += caller.value
        assert(!caller.value.observers.contains(this), "Cyclic Signal definition!")
        myValue
    }
    update(expr)

    protected def update(expr: => T): Unit = {
        myExp = () => expr
        computeValue()
    }

    protected def computeValue(): Unit = {
        val newValue = caller.withValue(this)(myExp())
        if (myValue != newValue) {
            myValue = newValue
            val obs = observers
            observers = Set()
            obs.foreach(_.computeValue())
        }
    }
}

object NoSignal extends Signal[Nothing](???) { 
    override def computeValue() = ()
 }

object Signal {
    private val caller = new DynamicVariable[Signal[_]](NoSignal)
    def apply[T](expr: => T) = new Signal(expr)
}