package frp
import org.scalajs.dom
import org.scalajs.dom.{Element}
import scala.scalajs.js
import scalatags.JsDom.all._


// /**
//  * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
//  */

object Framework {

//   /**
//    * Wraps reactive strings in spans, so they can be referenced/replaced
//    * when the Rx changes.
//    */
   
  implicit def SignalStr[T](r: SignalTrait[T])(implicit f: T => Modifier): Modifier = {
    rxMod(Signal(span(r())))
  }

//   /**
//    * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
//    * to propagate changes into the DOM via the element's ID. Monkey-patches
//    * the Obs onto the element itself so we have a reference to kill it when
//    * the element leaves the DOM (e.g. it gets deleted).
//    */

  implicit def rxMod(r: SignalTrait[HtmlTag]): Modifier = {
    def rSafe = r()

    var last = rSafe.render

    Obs(r){
        val newLast = rSafe.render
        last.parentElement.replaceChild(newLast, last)
        last = newLast
    }

   last
  }

  implicit def RxAttrValue[T: AttrValue] = new AttrValue[SignalTrait[T]]{
    def apply(t: Element, a: Attr, r: SignalTrait[T]): Unit = {
      Obs(r){ implicitly[AttrValue[T]].apply(t, a, r())}
    }
  }

  implicit def RxStyleValue[T: StyleValue] = new StyleValue[Signal[T]]{
    def apply(t: Element, s: Style, r: Signal[T]): Unit = {
      Obs(r){ implicitly[StyleValue[T]].apply(t, s, r())}
    }
  }

}