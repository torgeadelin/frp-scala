package bank
import frp._


class FRPBankAccount {
    var balance = Var(0)

    def currentBalance: Int = balance()

    def deposit(amount: Int): Unit = {
        if(amount > 0) {
            val b = balance()
            balance() = b + amount
          
        }
    }

    def withdraw(amount: Int): Unit = {
        if(amount > 0 && amount <= balance()) {
            val b = balance()
            balance() = b - amount
        } else throw new Error("Insufficient funds")
    }

   
}
