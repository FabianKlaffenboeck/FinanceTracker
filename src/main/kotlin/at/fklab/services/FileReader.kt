package at.fklab.services

import at.fklab.model.Transaction
import java.io.File

enum class FileTag {
    JSON, CSV
}

class ParsingTarget(
    var tag: FileTag,
    var file: File,
)

class FileReader {

    fun readFiles(parsingTargets: List<ParsingTarget>): List<Transaction> {
        val resultList: MutableList<Transaction> = mutableListOf()

        parsingTargets.forEach { parsingTarget ->
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
        var content = parsingTarget.file.bufferedReader().readLines()

        return outputList
    }

}