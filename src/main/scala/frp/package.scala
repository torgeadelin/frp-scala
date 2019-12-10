import annotation.tailrec

package object frp {
    object NoInitializedException extends Exception()

    @tailrec def propagate(nodes: Seq[(Flow.Emitter[Any], Flow.Reactor[Nothing])]): Unit = {
        if(nodes.length != 0) {
            val minLevel = nodes.minBy(_._2.level)._2.level 
            val (now, later) = nodes.partition(_._2.level == minLevel)
            val next = for {
                (target, pingers) <- now.groupBy(_._2).mapValues(_.map(_._1).distinct).toSeq
                nextTarget <- target.ping(pingers)
            } yield {
                target.asInstanceOf[Flow.Emitter[Any]] -> nextTarget
            }

            propagate(next ++ later)
        }
    }
}
