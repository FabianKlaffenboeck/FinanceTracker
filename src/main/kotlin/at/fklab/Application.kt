package at.fklab

import at.fklab.model.Transaction
import at.fklab.plugins.configureDatabases
import at.fklab.services.FileReader
import at.fklab.services.FileTag
import at.fklab.services.ParsingTarget
import java.io.File

fun main(args: Array<String>) {
    println("test")

    val dbUrl: String = System.getenv("DBURL") ?: "jdbc:sqlite:TestDB"
    val dbUser: String = System.getenv("DBUSER") ?: "root"
    val dbPW: String = System.getenv("DBPW") ?: ""
    val initDB: Boolean = System.getenv("INITDB").toBoolean()
    val populateDB: Boolean = System.getenv("POPULATEDB").toBoolean()

    configureDatabases(dbUrl, dbUser, dbPW, initDB, populateDB)


    val parsingTargets = listOf(ParsingTarget(tag = FileTag.JSON, file = File("")))

    FileReader().readFiles(parsingTargets)
}