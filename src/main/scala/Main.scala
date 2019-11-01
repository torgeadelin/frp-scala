import bank._ 
import frp._

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
    
    println()
    println("################")
    //Bank accounts
    val bank1 = new BankAccount
    val bank2 = new BankAccount
    val bank3 = new BankAccount

    //Consolidator monitoring all bank accounts above
    val cons = new Consolidator(List(bank1, bank2, bank3))

    println(s"Total balance for Cons = ${cons.totalBalance}")
    println("Deposit 100 in Bank 1")
    bank1.deposit(100)
    println(s"Total balance for Cons = ${cons.totalBalance}")
    println("Deposit 30 in Bank 2")
    bank2.deposit(30)
    println(s"Total balance for Cons = ${cons.totalBalance}")

    println("################")
    println()   
    println("################")
    
    //========================================================================================
    /*                                                                                      *
    *                                  FRP Implementation                                   *
    *                                                                                       */
    //========================================================================================

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

    println("################")
    println()
}