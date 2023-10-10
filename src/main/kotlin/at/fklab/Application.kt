package at.fklab

import at.fklab.plugins.configureDatabases
import io.ktor.server.application.*

fun main(args : Array<String>) {
    println("test")

    val dbUrl: String = System.getenv("DBURL") ?: "jdbc:sqlite:TestDB"
    val dbUser: String = System.getenv("DBUSER") ?: "root"
    val dbPW: String = System.getenv("DBPW") ?: ""
    val initDB: Boolean = System.getenv("INITDB").toBoolean()
    val populateDB: Boolean = System.getenv("POPULATEDB").toBoolean()

    configureDatabases(dbUrl, dbUser, dbPW, initDB, populateDB)
}