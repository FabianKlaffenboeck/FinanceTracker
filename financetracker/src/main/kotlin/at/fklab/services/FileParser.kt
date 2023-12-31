package at.fklab.services

import at.fklab.model.Transaction
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern


enum class FileTag {
    NON, JSON, CSV
}

class ParsingTarget(
    var tag: FileTag,
    var file: File,
)

class FileParser {

    fun pars(fileDIR: String): List<Transaction> {
        return removeDuplicates(parsFile(generateTargets(fileDIR)))
    }

    private fun removeDuplicates(inputData: List<Transaction>): List<Transaction> {
        var outputList = mutableListOf<Transaction>()

        inputData.forEach { data ->
            if (outputList.none { (it.value == data.value) && (it.usage == data.usage) && (it.category == data.category) && (it.cardType == data.cardType) }) {
                outputList.add(data)
            }
        }

        return outputList
    }

    private fun parsFile(parsingTargets: List<ParsingTarget>): List<Transaction> {
        val resultList: MutableList<Transaction> = mutableListOf()

        parsingTargets.forEach { parsingTarget: ParsingTarget ->
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

    private fun generateTargets(fileDIR: String): MutableList<ParsingTarget> {
        val parsingTargets: MutableList<ParsingTarget> = mutableListOf()
        File(fileDIR).listFiles()?.forEach { file ->
            var fileTag: FileTag = FileTag.NON

            if (file.path.contains("json")) {
                fileTag = FileTag.JSON
            }
            if (file.path.contains("csv")) {
                fileTag = FileTag.CSV
            }
            parsingTargets.add(ParsingTarget(tag = fileTag, file = File(file.path)))
        }
        return parsingTargets
    }

    private fun parsJson(parsingTarget: ParsingTarget): List<Transaction> {
        val outputList: MutableList<Transaction> = mutableListOf()
        val content = parsingTarget.file.readText(Charsets.UTF_8)

        val map = Gson().fromJson<Map<*, *>>(content, MutableMap::class.java)
        val dataList = map["list"] as ArrayList<Map<String, String>>

        dataList.forEach { it ->

            val tmpValues = it["betrag"] as LinkedTreeMap<String, String>
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val tmpTransaction = Transaction(
                id = null,
                value = (tmpValues["amount"] as Double).toFloat(),
                currency = tmpValues["currency"],
                category = it["kategorisierungsart"],
                usage = it["verwendungszweckZeile1"] ?: it["geschaeftsfallCode"],
                transactionPartner = it["auftraggeberIban"] ?: it["haendlername"],
                transactionDate = LocalDate.parse(it["buchungstag"]?.substring(0, 10), formatter),
                debitDate = LocalDate.parse(it["einfuegezeit"]?.substring(0, 10), formatter),
                bookingId = it["id"],
                location = it["haendlerort"],
                cardType = it["kartenId"] ?: it["iban"],
                sourceFile = parsingTarget.file.name
            )
            outputList.add(tmpTransaction)
        }
        return outputList
    }

    private fun parsCsv(parsingTarget: ParsingTarget): List<Transaction> {
        val outputList: MutableList<Transaction> = mutableListOf()
        val content = parsingTarget.file.bufferedReader().readLines().toMutableList()
        content[0] = content[0].drop(1) //FIXME drop is used because of a zero with character

        content.forEach { line: String ->

            val columns = line.split(';')
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            val tmpTransaction = Transaction(
                id = null,
                value = columns[3].replace(',', '.').toFloat(),
                currency = columns[4],
                category = null,
                usage = determineUsage(columns[1]),
                transactionPartner = null,
                transactionDate = LocalDate.parse(
                    columns[5].substring(0, 10), formatter
                ), //there is a lot of seconds that (at least for now) no-one uses
                debitDate = LocalDate.parse(columns[0], formatter),
                bookingId = null,
                location = null,
                cardType = fileNaneToIban(parsingTarget.file.name),
                sourceFile = parsingTarget.file.name
            )
            outputList.add(tmpTransaction)
        }
        return outputList
    }

    private fun determineUsage(data: String): String {

        var startingIndex = data.indexOf("Zahlungsreferenz")
        if (startingIndex == -1) {
            startingIndex = data.indexOf("Verwendungszweck")
        }

        if (startingIndex == -1) {
            startingIndex = 0
        }

        var tmp = data.substring(startingIndex, data.length)
        tmp = tmp.substring(tmp.indexOf(':') + 2, tmp.length)

        var endIndex = tmp.indexOf(':')
        if (endIndex != -1) {
            while (tmp[endIndex] != ' ') {
                endIndex--
            }
        } else {
            endIndex = tmp.length
        }

        return tmp.substring(0, endIndex)
    }

    private fun fileNaneToIban(fileName: String): String {
        val pattern = Pattern.compile("[A-Z]{2}\\d{2} ?\\d{4} ?\\d{4} ?\\d{4} ?\\d{4} ?[\\d]{0,2}", Pattern.MULTILINE)
        val matcher = pattern.matcher(fileName)
        while (matcher.find()) {
            return matcher.group(0)
        }
        return ""
    }
}