 package observable

 trait Subscriber {
    def handler(pub: Publisher)
 }
