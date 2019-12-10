package frp

import util.Try
import collection.mutable

object Flow {
    trait Emitter[+T] extends Node {
        private[this] val children: mutable.HashMap[Reactor[T], Unit] = new mutable.HashMap()

        def getChildren: Seq[Reactor[Nothing]] = children.keys.toSeq

        def linkChild[R >: T](child: Reactor[R]) = children(child) = ()
    }

    trait Reactor[-T] extends Node {
        def getParents: Seq[Emitter[Any]]
        
        def ping(incoming: Seq[Emitter[Any]]): Seq[Reactor[Nothing]]
    }

    trait Node {
        def level: Long
        def name: String
        val id: String = util.Random.alphanumeric.head.toString

        def debug(s: String) {
            println(id + ": " + s)
        }
    }
}