package at.fklab.services

import java.io.File

enum class FileTag {
    JSON, CSV
}

class ParsingTarget(
    var tag: FileTag,
    var file: File,
)

class FileReader {
    val parsingTargets = listOf(ParsingTarget(tag = FileTag.JSON, file = File("")))

}