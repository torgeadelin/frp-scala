import bank._ 
import frp._

import org.scalajs.dom
import scalatags.JsDom.all._
import scalatags.Text
import dom.document
import scala.scalajs.js.annotation.JSExport


@JSExport
object Main extends App {

    //========================================================================================
    /*                                                                                      *
    *                               Observer Pattern Example                                *
    *                                                                                       */
    //========================================================================================

    //* Advantages to this design pattern ✅
    // Decouples view from state
    // Allows to have a varying number of views for a given state
    // Simple to set up

    //! Disadvantages to this design pattern ❌
    // Forces imperative style, since handler are Units
    // Many moving parts that need to be coordinated
    // Concurrency makes things more complicated ( Single view that listens to two Models that update concurrently)
    // Views are still tightly bound to one state; view update happens immediately (not always good, too many re-renders)
    
    // println()
    // println("################")
    //Bank accounts

    val bank1 = new BankAccount
    val bank2 = new BankAccount
    val bank3 = new BankAccount

    //Consolidator monitoring all bank accounts above
    val cons = new Consolidator(List(bank1, bank2, bank3))

    // println(s"Total balance for Cons = ${cons.totalBalance}")
    // println("Deposit 100 in Bank 1")
    // bank1.deposit(100)
    // println(s"Total balance for Cons = ${cons.totalBalance}")
    // println("Deposit 30 in Bank 2")
    // bank2.deposit(30)
    // println(s"Total balance for Cons = ${cons.totalBalance}")

    // println("################")
    // println()   
    // println("################")
    
    //========================================================================================
    /*                                                                                      *
    *                                  FRP Implementation                                   *
    *                                                                                       */
    //========================================================================================

    // val frp_bank1 = new FRPBankAccount
    // val frp_bank2 = new FRPBankAccount
    // val frp_bank3 = new FRPBankAccount


    // def frp_consolidator(accounts: List[FRPBankAccount]): Signal[Int] =
    //     Signal(accounts.map(_.balance()).sum)

    // val c = frp_consolidator(List(frp_bank1, frp_bank2, frp_bank3))
    
    // println(s"Total balance for Cons = ${c()}")
    // println("Deposit 100 in Bank 1")
    // frp_bank1.deposit(100)
    // println(s"Total balance for Cons = ${c()}")
    // println("Deposit 30 in Bank 2")
    // frp_bank2.deposit(30)
    // println(s"Total balance for Cons = ${c()}")

    val a = new Var(1)
    val b = new Signal(2)

    def fun(a: String) = b() + 1

    a() = fun("lol")



    // println("################")
    // println()

    //helpers

    // def elementValueSignal(element: html.Element, getValue: () => String): Signal[String] = {

    //     var prevVal = getValue()
    //     val value = new Var(prevVal)

    //     // function that gets called on certain dom event
    //     val onChange = { (event: dom.Event) =>
    //         // Reconstruct manually the optimization at the root of the graph
    //         val newVal = getValue()

    //         if (newVal != prevVal) {
    //             prevVal = newVal
    //             value() = newVal
    //         }
    //     }

    //     element.addEventListener("change", onChange)
    //     element.addEventListener("keypress", onChange)
    //     element.addEventListener("keyup", onChange)

    //     value
    // }

    // //get input from HTML.Input and create signal, and update signal whenever Input changes
    // def inputValueSignal(input: html.Input): Signal[String] =
    //     elementValueSignal(input, () => input.value)

    // //get input from HTML.TextArea and create signal, and update signal whenever Input changes
    // def textAreaValueSignal(textAreaID: String): Signal[String] = {
    //     val textArea = document.getElementById(textAreaID).asInstanceOf[html.TextArea]
    //     elementValueSignal(textArea, () => textArea.value)
    // }


    // val c = frp_consolidator(List(frp_bank1, frp_bank2, frp_bank3))

    // dom.document.body.innerHTML = ""
    // dom.document.body.appendChild(
    //     div(
    //         h1("Hello world!"),
    //         div("Deposit 100 in bank1 and c = ", onclick := {() => {
    //             frp_bank1.deposit(100) 
    //             println(c())
    //         }}),
    //         div("Value of c = ",  c())
    //     ).render
    // )
    
}