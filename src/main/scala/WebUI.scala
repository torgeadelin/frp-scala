package frp
import frp._
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.html
import dom.document

object WebUI {
    def main(args: Array[String]): Unit = {
    try {
        setupLabel()
  
        } 
    catch {
        case th: Throwable =>
            th.printStackTrace()
        }
    }

    //──── Helper methods ────────────────────────────────────────────────────────────────────

    // Returns dom element by id
    def elementById[A <: js.Any](id: String): A =
        document.getElementById(id).asInstanceOf[A]

    
    // Turns the html element content into a signal of a String ( Used for Input, Textarea)
    // and it changes based on change, key press or key up events
    def elementValueSignal(element: html.Element, getValue: () => String): Signal[String] = {
        var prevVal = getValue()
        val value = new Var(prevVal)
        val onChange = { (event: dom.Event) =>
            // Reconstruct manually the optimization at the root of the graph
            val newVal = getValue()
            if (newVal != prevVal) {
            prevVal = newVal
            value() = newVal
            }
        }

        element.addEventListener("change", onChange)
        element.addEventListener("keypress", onChange)
        element.addEventListener("keyup", onChange)
        value
    }

    // Input to Signal[String]
    def inputValueSignal(input: html.Input): Signal[String] =
        elementValueSignal(input, () => input.value)

    // Input to Signal[String]
    def textAreaValueSignal(textAreaID: String): Signal[String] = {
        val textArea = elementById[html.TextArea](textAreaID)
        elementValueSignal(textArea, () => textArea.value)
    }

    def getLength(string: Signal[String]): Signal[Int] = 
       new Signal[Int](string().length)

    def getColor(len: Signal[Int]): Signal[String] = {
        new Signal[String](if (len() < 10) "green" else "red")
    }
    

     def setupLabel(): Unit = {
        val text =  textAreaValueSignal("area_target")

        val countLabel = document.getElementById("label_target").asInstanceOf[html.Span]
 
        // If you define len = getLength(tweenText()) where getLength returns a Signal[Int] it won't work
        // because the len will depend on the function getLength instead of another signal 
        // ?It works with the color but not with len? (using the function) I think it has to do with the text() call
        // *It is, it works if I use text, and refactor the getLegth method.

        val len =  getLength(text)
        val color = getColor(len)
        println(color())
        println(len())

        // Gets re evaluated whenever charCont gets changed
        // Set the text content of countLabel which is a <span> with the value of len
        // Acts like an Obs (which I have to implement)
        // TODO Implement Observer
        Signal {
            countLabel.textContent =  len().toString
        }

        Signal {
            countLabel.style.color = color()
        }
    }
}