//========================================================================================
/*                                                                                      *
 *                               Publish Subscribe Pattern                              *
 *                                                                                      */
//========================================================================================

// package bank
// import observable._

// class Consolidator(observed: List[BankAccount]) extends Subscriber {
//     observed.foreach(_.subscribe(this))

//     //Initially is not initialized (_)
//     private var total: Int = _
//     compute()

//     private def compute() = 
//         total = observed.map(_.currentBalance).sum

//     def handler(pub: Publisher) = compute()
    
//     //Accessor method
//     def totalBalance = total
//  }