package at.fklab.services

import at.fklab.model.Transaction
import at.fklab.model.TransactionEntity
import at.fklab.model.Transactions
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class TransactionService {

    fun getAll(): List<Transaction> = transaction {
        val query = Op.build { Transactions.deletedAt.isNull() }
        TransactionEntity.find(query).map(TransactionEntity::toTransaction)
    }

    fun getById(id: Int): Transaction? = transaction {
        TransactionEntity.find {
            Transactions.id eq id
        }.firstOrNull()?.toTransaction()
    }

    fun add(transaction: Transaction): Transaction = transaction {
        TransactionEntity.new {

            value = transaction.value
            currency = transaction.currency
            category = transaction.category
            usage = transaction.usage
            transactionPartner = transaction.transactionPartner
            transactionDate = transaction.transactionDate
            debitDate = transaction.debitDate
            bookingId = transaction.bookingId
            location = transaction.location
            cardType = transaction.cardType
            sourceFile = transaction.sourceFile

            createdAt = LocalDateTime.now()
        }.toTransaction()
    }

    fun update(transaction: Transaction): Transaction = transaction {
        val notNullId = transaction.id ?: -1

        TransactionEntity[notNullId].value = transaction.value
        TransactionEntity[notNullId].currency = transaction.currency
        TransactionEntity[notNullId].category = transaction.category
        TransactionEntity[notNullId].usage = transaction.usage
        TransactionEntity[notNullId].transactionPartner = transaction.transactionPartner
        TransactionEntity[notNullId].transactionDate = transaction.transactionDate
        TransactionEntity[notNullId].debitDate = transaction.debitDate
        TransactionEntity[notNullId].bookingId = transaction.bookingId
        TransactionEntity[notNullId].location = transaction.location
        TransactionEntity[notNullId].cardType = transaction.cardType
        TransactionEntity[notNullId].sourceFile = transaction.sourceFile

        TransactionEntity[notNullId].updatedAt = LocalDateTime.now()
        TransactionEntity[notNullId].toTransaction()
    }

    fun delete(id: Int) = transaction {
        TransactionEntity[id].deletedAt = LocalDateTime.now()
    }
}