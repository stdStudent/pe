package std.student.headers.importTable.funcion

import std.student.conventions.QWord
import std.student.headers.Header
import std.student.headers.importTable.funcion.elements.ImportFunctionName
import java.io.RandomAccessFile

data class ImportFunction(
    val name: ImportFunctionName,
): Header {
    companion object {
        fun get(functionNameOffset: QWord, file: RandomAccessFile) =
            ImportFunction(name = ImportFunctionName(functionNameOffset, file))
    }

    override val headerName = "Function"

    override fun toString(): String {
        return """
            |$headerName:
            |    ${name.realName}: $name
        """.trimMargin()
    }
}