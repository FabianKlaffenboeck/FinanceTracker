package at.fklab.services

import at.fklab.model.Transaction
import java.io.File

enum class FileTag {
    NON, JSON, CSV
}

class ParsingTarget(
    var tag: FileTag,
    var file: File,
)

class FileParser {

    fun parsFile(parsingTargets: List<ParsingTarget>): List<Transaction> {
        val resultList: MutableList<Transaction> = mutableListOf()

        parsingTargets.forEach { parsingTarget: ParsingTarget ->
            println(parsingTarget)

            when (parsingTarget.tag) {
                FileTag.JSON -> {
                    resultList.addAll(parsJson(parsingTarget))
                }

                FileTag.CSV -> {
                    resultList.addAll(parsCsv(parsingTarget))
                }

                else -> {
                    println("error")
                }
            }
        }

        return resultList
    }

    private fun parsJson(parsingTarget: ParsingTarget): List<Transaction> {
        val outputList: MutableList<Transaction> = mutableListOf()
        var content = parsingTarget.file.readText(Charsets.UTF_8)

        return outputList
    }

    private fun parsCsv(parsingTarget: ParsingTarget): List<Transaction> {
        val outputList: MutableList<Transaction> = mutableListOf()
        val content = parsingTarget.file.bufferedReader().readLines()

        content.forEach { line: String ->

            val columns = line.split(';')

            columns[1]

            var tmpTransaction = Transaction(
                id = null,
                value = columns[0].toFloat(),
                currency = columns[4],
                category = null,
                usage = null,
                transactionPartner = null,
                transactionDate = null,
                bookingId = null,
                location = null,
                cardType = null,
            )

            outputList.add(tmpTransaction)
        }

        return outputList
    }
}