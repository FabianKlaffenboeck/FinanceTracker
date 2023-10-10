package at.fklab

import at.fklab.model.Transaction
import at.fklab.plugins.configureDatabases
import at.fklab.services.TransactionService

fun main(args: Array<String>) {
    println("test")

    val dbUrl: String = System.getenv("DBURL") ?: "jdbc:sqlite:TestDB"
    val dbUser: String = System.getenv("DBUSER") ?: "root"
    val dbPW: String = System.getenv("DBPW") ?: ""
    val initDB: Boolean = System.getenv("INITDB").toBoolean()
    val populateDB: Boolean = System.getenv("POPULATEDB").toBoolean()

    configureDatabases(dbUrl, dbUser, dbPW, initDB, populateDB)

    for (i in 1..5) {
        TransactionService().add(
            Transaction(
                id = null,
                value = null,
                currency = null,
                category = null,
                usage = null,
                transactionPartner = null,
                transactionDate = null,
                bookingId = null,
                location = null
            )
        )
    }


}