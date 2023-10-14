package at.fklab

import at.fklab.plugins.configureDatabases
import at.fklab.services.FileParser
import at.fklab.services.FileTag
import at.fklab.services.ParsingTarget
import java.io.File

fun main(args: Array<String>) {

    val dbUrl: String = System.getenv("DBURL") ?: "jdbc:sqlite:TestDB"
    val dbUser: String = System.getenv("DBUSER") ?: "root"
    val dbPW: String = System.getenv("DBPW") ?: ""
    val initDB: Boolean = System.getenv("INITDB").toBoolean()

    val fileDIR: String = System.getenv("FILEDIR")

    configureDatabases(dbUrl, dbUser, dbPW, initDB)

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

    FileParser().parsFile(parsingTargets)
}