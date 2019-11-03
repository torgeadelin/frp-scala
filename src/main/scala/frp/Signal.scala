package frp
import scala.util.DynamicVariable


// Each signal maintains 
// - its current time
// - the current expression that defines the signal value
// - a set of observers: the other signals that depend on its value 

// If the signal changes, all observers must be updated

class Signal[T](expr: => T) {
    import Signal._

    // The expression of the signal
    private var myExp: () => T = _ 

    private var myValue: T = _ 
    
    // Who's listening to this signal?
    private var observers: Set[Signal[_]] = Set()
   
    // same as Signal(...)
    def apply(): T = {
       
        //caller is the left hand side, 
        //so we're just adding this caller as a dependent to our signal
        //example: a() = b() + 1,  caller is b() and observes a() aka it is its dependency
        observers += caller.value

        println(caller)
        println("Signal")
        //make sure the observers of b() don't contain a()
        assert(!caller.value.observers.contains(this), "Cyclic Signal definition!")

        //return the value at this time
        myValue
    }
    
    //Whenever we create a signal we update the expression
    update(expr)

    //Apply the expression to myExp
    protected def update(expr: => T): Unit = {
        myExp = () => expr

        //compute value
        computeValue()
    }

    protected def computeValue(): Unit = {
        //evaluate
        val newValue = caller.withValue(this)(myExp()) 

        println(s"this = ${this}")
        println(s"my expr  as func =  ${myExp()}")
        println(s"my value = ${myValue}")
        println(s"new value = ${newValue}")

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