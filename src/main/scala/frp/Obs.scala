package frp

case class Obs(name: String, es: Seq[Flow.Emitter[Any]])(callback: () => Unit) extends Flow.Reactor[Any] {
    var parents: Seq[Flow.Emitter[Any]] = es
    @volatile var active = true
    es.foreach(_.linkChild(this))
    def getParents = parents
    def level = Long.MaxValue

    def ping(incoming: Seq[Flow.Emitter[Any]]) = {
        if(active && getParents.intersect(incoming).isDefinedAt(0)) {
            util.Try(callback())
        }
        Nil
    }

    def trigger() = {
        this.ping(this.getParents)
    }
}

object Obs {
    def apply[T](es: Flow.Emitter[Any]*)(callback: => Unit) = {
        new Obs("", es.toSeq)(() => callback)
    }

    def cfg[T](name: String)(es: Flow.Emitter[Any]*)(callback: => Unit) = {
        new Obs(name, es.toSeq)(() => callback)
    }
}