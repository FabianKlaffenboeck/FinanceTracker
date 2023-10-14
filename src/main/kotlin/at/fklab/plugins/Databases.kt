package at.fklab.plugins

import at.fklab.model.Transactions
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabases(dbUrl: String, dbUser: String, dbPW: String, initDB: Boolean) {

    val database = Database.connect(
        url = dbUrl, user = dbUser, password = dbPW
    )

    if (initDB) {
        transaction {
            SchemaUtils.drop(Transactions)
        }
        transaction {
            SchemaUtils.create(Transactions)
        }
    }
}