import bank._ 
import frp._
import Framework._

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.html
import dom.document
import scala.scalajs.js.annotation._
import scalatags.JsDom.all._
import org.scalajs.dom.{KeyboardEvent, Event}



@JSExportTopLevel("Main")
object Main extends App {

//     //========================================================================================
//     /*                                                                                      *
//     *                               Observer Pattern Example                                *
//     *                                                                                       */
//     //========================================================================================

//     //* Advantages to this design pattern ✅
//     // Decouples view from state
//     // Allows to have a varying number of views for a given state
//     // Simple to set up

//     //! Disadvantages to this design pattern ❌
//     // Forces imperative style, since handler are Units
//     // Many moving parts that need to be coordinated
//     // Concurrency makes things more complicated ( Single view that listens to two Models that update concurrently)
//     // Views are still tightly bound to one state; view update happens immediately (not always good, too many re-renders)
    
//     println()
//     println("################")
//     //Bank accounts

//     val bank1 = new BankAccount
//     val bank2 = new BankAccount
//     val bank3 = new BankAccount

//     //Consolidator monitoring all bank accounts above
//     val cons = new Consolidator(List(bank1, bank2, bank3))

//     println(s"Total balance for Cons = ${cons.totalBalance}")
//     println("Deposit 100 in Bank 1")
//     bank1.deposit(100)
//     println(s"Total balance for Cons = ${cons.totalBalance}")
//     println("Deposit 30 in Bank 2")
//     bank2.deposit(30)
//     println(s"Total balance for Cons = ${cons.totalBalance}")

//     println("################")
//     println()   
//     println("################")
    
//     //========================================================================================
//     /*                                                                                      *
//     *                                  FRP Implementation                                   *
//     *                                                                                       */
//     //========================================================================================

    val frp_bank1 = new FRPBankAccount
    val frp_bank2 = new FRPBankAccount
    val frp_bank3 = new FRPBankAccount


    def frp_consolidator(accounts: List[FRPBankAccount]): Signal[Int] =
        Signal(accounts.map(_.balance()).sum)

    val c = frp_consolidator(List(frp_bank1, frp_bank2, frp_bank3))
    
    println(s"Total balance for Cons = ${c()}")
    println("Deposit 100 in Bank 1")
    frp_bank1.deposit(100)
    println(s"Total balance for Cons = ${c()}")
    println("Deposit 30 in Bank 2")
    frp_bank2.deposit(30)
    println(s"Total balance for Cons = ${c()}")

    val a = Var(1)
    val b = Signal(2)

    def fun(a: String) = b() + 1

    a() = fun("lol")

    println("################")
    println()


   

    //========================================================================================
    /*                                                                                       *
    *                                       WebUI Part                                       *
    *                                                                                        */
    //========================================================================================

    // Implemented a small textarea and a counter which changes its color depending on the number
    // of characters.

    // try {
    //     setupLabel()
    // } catch {
    //     case th: Throwable =>
    //         th.printStackTrace()
    // }
        
    // //──── Helper methods ────────────────────────────────────────────────────────────────────

    // // Returns dom element by id
    // def elementById[A <: js.Any](id: String): A =
    //     document.getElementById(id).asInstanceOf[A]

    
    // // Turns the html element content into a signal of a String ( Used for Input, Textarea)
    // // and it changes based on change, key press or key up events
    // def elementValueSignal(element: html.Element, getValue: () => String): Signal[String] = {
    //     var prevVal = getValue()
    //     val value = Var(prevVal)
    //     val onChange = { (event: dom.Event) =>
    //         // Reconstruct manually the optimization at the root of the graph
    //         val newVal = getValue()
    //         if (newVal != prevVal) {
    //         prevVal = newVal
    //         value() = newVal
    //         }
    //     }

    //     element.addEventListener("change", onChange)
    //     element.addEventListener("keypress", onChange)
    //     element.addEventListener("keyup", onChange)
    //     Signal(value())
    // }

    // // Input to Signal[String]
    // def inputValueSignal(input: html.Input): Signal[String] =
    //     elementValueSignal(input, () => input.value)

    // // Input to Signal[String]
    // def textAreaValueSignal(textAreaID: String): Signal[String] = {
    //     val textArea = elementById[html.TextArea](textAreaID)
    //     elementValueSignal(textArea, () => textArea.value)
    // }

    // def getLength(string: Signal[String]): Signal[Int] = 
    //     Signal[Int](string().length)

    // def getColor(len: Signal[Int]): Signal[String] = {
    //     Signal[String](if (len() < 10) "green" else "red")
    // }
    

    //  def setupLabel(): Unit = {
    //     val text =  textAreaValueSignal("area_target")

    //     val countLabel = document.getElementById("label_target").asInstanceOf[html.Span]
 
    //     // If you define len = getLength(tweenText()) where getLength returns a Signal[Int] it won't work
    //     // because the len will depend on the function getLength instead of another signal 
    //     // ?It works with the color but not with len? (using the function) I think it has to do with the text() call
    //     // *It is, it works if I use text, and refactor the getLegth method.

    //     val len =  getLength(text)
    //     val color = getColor(len)
    //     println(color())
    //     println(len())

    //     // Gets re evaluated whenever charCont gets changed
    //     // Set the text content of countLabel which is a <span> with the value of len
    //     // Acts like an Obs (which I have to implement)
    //     // TODO Implement Observer
    //     Signal {
    //         countLabel.textContent =  len().toString
    //     }

    //     Signal {
    //         countLabel.style.color = color()
    //     }
    // }

    val z = Var(3)
    val y = Signal {
        5 / z()
    }

   
    val textLength = Var(0)
    val textColor = Signal {
        if (textLength() < 20) "green" else "red"
    }


    val textAreaComponent = input(
                                cls:="form-control", 
                                id:="area_target",   
                                          
    ).render

    textAreaComponent.onkeyup = (e: KeyboardEvent) => {
        textLength() = textAreaComponent.value.length
    }

    dom.document.body.innerHTML = ""
    dom.document.body.appendChild(
        div( cls := "container pt-5",
            h1("HSmall FRP Implementation is Scala! Now compatible with Scala tags!"),
             div(cls :="form-group",
                label(
                    "Example textarea. Count color will change to ",
                    span(color:= "red", "red "),
                    "if there are more than 10 characters, or ",
                    span( color:= "green", "green "),
                    "otherwise."
                ),
                textAreaComponent
            ),
            p("Count ",color:=textColor, textLength),        
        ).render
    )

}