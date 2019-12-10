package frp
import scala.util.DynamicVariable
import util.Try

// Must read: https://stackoverflow.com/questions/39928480/how-to-implement-frp-using-implicit-parameter

// Each signal maintains 
// - its current time
// - the current expression that defines the signal value
// - a set of observers: the other signals that depend on its value 

// If the signal changes, all observers must be updated

import collection.mutable
import util.Try


trait SignalTrait[+T] extends Flow.Emitter[T]{
    def currentValue: T

    def now: T = currentValue

    def apply(): T = {
        val current = Signal.enclosing.value

        if(current != null) {
            this.linkChild(Signal.enclosingR.value)
            Signal.enclosing.value = current.copy(
                level = math.max(this.level + 1, current.level),
                parents = this +: current.parents
            )
        }
        currentValue
    }

    def toTry: Try[T]
}

case class SigState[+T](
    parents: Seq[Flow.Emitter[Any]],
    value: Try[T],
    level: Long
)

class Signal[+T](val name: String, calc: () => T) extends SignalTrait[T] with Flow.Reactor[Any] {

    // The expression of the signal
    @volatile var active = true
    @volatile private[this] var state: SigState[T] = fullCalc(Option(Signal.enclosing.value).map(_.level + 1).getOrElse(0))

    def fullCalc(lvl: Long = level): SigState[T] = {
        Signal.enclosing.withValue(SigState(Nil, null, lvl)) {
            Signal.enclosingR.withValue(this) {
                val newValue = Try(calc())
                Signal.enclosing.value.copy(value = newValue)
            }
        }
    }
    def ping(incoming: Seq[Flow.Emitter[Any]]) = {
        println("ping")
        if(active && getParents.intersect(incoming).isDefinedAt(0)) {
            val newState = fullCalc()

            val enclosingLevel: Long = Option(Signal.enclosing.value).map(_.level + 1).getOrElse(0)
            val newLevel = math.max(newState.level, enclosingLevel)

            state = newState.copy(newState.parents, newState.value, newLevel)
            getChildren
        } else {
            Nil
        }
    }

    def getParents = state.parents

    def toTry = state.value
    def level = state.level
    def currentValue = state.value.get

}


object Signal {
    val enclosing = new DynamicVariable[SigState[Any]](null)
    val enclosingR = new DynamicVariable[Flow.Reactor[Any]](null)

    def apply[T](calc: => T)(implicit name: String = ""): Signal[T] = new Signal(name, () => calc)
}