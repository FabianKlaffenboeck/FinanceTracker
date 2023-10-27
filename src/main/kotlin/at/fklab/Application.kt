package at.fklab

import at.fklab.plugins.configureDatabases
import at.fklab.services.FileParser
import at.fklab.services.TransactionService

fun main(args: Array<String>) {

    val dbUrl: String = System.getenv("DBURL") ?: "jdbc:sqlite:TestDB"
    val dbUser: String = System.getenv("DBUSER") ?: "root"
    val dbPW: String = System.getenv("DBPW") ?: ""
    val initDB: Boolean = System.getenv("INITDB").toBoolean()

    val fileDIR: String = System.getenv("FILEDIR")

    configureDatabases(dbUrl, dbUser, dbPW, initDB)

    val transactionService = TransactionService()
    val fileParser = FileParser()

    val tmp = fileParser.pars(fileDIR)

    tmp.forEach { transaction -> transactionService.add(transaction) }
}