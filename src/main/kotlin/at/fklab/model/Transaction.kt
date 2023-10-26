package at.fklab.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDate


object Transactions : IntIdTable("Transactions") {

    val value = float("value").nullable()
    val currency = text("currency").nullable()

    val category = text("category").nullable()
    val usage = text("usage").nullable()

    val transactionPartner = text("transactionPartner").nullable()
    val transactionDate = date("transactionDate").nullable()
    val debitDate = date("debitDate").nullable()

    val bookingId = text("bookingId").nullable()
    val location = text("location").nullable()

    val cardType = text("cardType").nullable()

    val sourceFile = text("sourceFile").nullable()

    val createdAt = datetime("createdAt").nullable()
    val updatedAt = datetime("updatedAt").nullable()
    val deletedAt = datetime("deletedAt").nullable()
}

class TransactionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TransactionEntity>(Transactions)

    var value by Transactions.value
    var currency by Transactions.currency

    var category by Transactions.category
    var usage by Transactions.usage

    var transactionPartner by Transactions.transactionPartner
    var transactionDate by Transactions.transactionDate
    var debitDate by Transactions.debitDate

    var bookingId by Transactions.bookingId
    var location by Transactions.location

    var cardType by Transactions.cardType

    var sourceFile by Transactions.sourceFile

    var createdAt by Transactions.createdAt
    var updatedAt by Transactions.updatedAt
    var deletedAt by Transactions.deletedAt

    fun toTransaction() = Transaction(
        id.value,
        value,
        currency,
        category,
        usage,
        transactionPartner,
        transactionDate,
        debitDate,
        bookingId,
        location,
        cardType,
        sourceFile
    )
}

class Transaction(
    var id: Int?,

    var value: Float?,
    var currency: String?,

    var category: String?,
    var usage: String?,

    var transactionPartner: String?,
    var transactionDate: LocalDate?, // Date of payment at terminal / online
    var debitDate: LocalDate?, // Date of money removal from account

    var bookingId: String?,
    var location: String?,

    var cardType: String?,

    var sourceFile: String?
)