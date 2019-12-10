package frp

import util.{Try, Success}
import java.util.concurrent.atomic.AtomicReference

case class Var[T](name: String, initValue: T) extends SignalTrait[T] with Call.Reactor[T] {
    val currentValueHolder = new AtomicReference[Try[T]](Success(initValue))
    def level = 0L

    def currentValue = toTry.get
    def update(newValue: Try[T]): Unit = {
        currentValueHolder.set(newValue)
        propagate(this.getChildren.map(this -> _))
    }

    def update(newValue: T): Unit = {
        currentValueHolder.set(Success(newValue))
        propagate(this.getChildren.map(this -> _))
    }

    def toTry = currentValueHolder.get
    def update(calc: T => T): Unit = {
        val oldValue = currentValue
        val newValue = calc(oldValue)
        if(!currentValueHolder.compareAndSet(Success(oldValue), Success(newValue))) update(calc)
        propagate(this.getChildren.map(this -> _))
    }
}

object Var {
    def apply[T](value: T)(implicit name: String = "") =  {
        new Var(name, value)
    }
}