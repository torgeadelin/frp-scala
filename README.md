# frp-scala

Small Implementation of FRP based on Signals (time-varying values) based on Deprecating the Observer Pattern with Scala.React by Ingo Maier and Martin Odersky

## Instructions to run

1. Install [sbt](https://www.scala-sbt.org/) (if not already)
2. Clone the project using `git clone`
3. Go to the project directory `cd frp-scala`
4. Run `sbt` in your terminal
5. Once sbt has started type `run`

## Documentation

###How?
When evaluating a signal-value expression we need to know which signal caller gets defined or updated by the expression. If we know who the caller is, then executing for example `sig()` => we add the caller to the observers of the `sig`.

###Who's calling?
The simple way to to do this is to maintain a global data structure that refers to the current caller and that will update as we evaluate signals.

### Signal and Val

- hold the value at the current time
- hold the current expression that defines the signal value
- hold a set with all signals that depend on themselves "list of observers". If the signal/val's value updates, then all observers must be re-evaluated
- the main difference between Signal and Val is that Val can be updated and Signal can't

###Creating a Signal

```scala
val sig:Signal[Int] = new Signal(10) //initial value = 10
println(sig()) //prints the current value of the signal
```

###Creating a Signal

```scala
val sig:Val[Int] = new Val(10) //initial value = 10
println(sig()) //prints the current value of the signal
sig() = 30
println(sig()) //signal got updated, and now is 30
```

### Cyclic Signals

When creating a signal, make sure you don't try to create an cyclic signal, because it doesn't make sense.

Example

```scala
val reactive: Val[Int] = reactive() + 1
```

throws an exception. Instead, write

```scala
val aux = reactive()
reactive() = aux + 1
```

###Next step?
Let's go on the web!
