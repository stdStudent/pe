package std.student.headers.importTable.funcion

import std.student.conventions.DWord
import std.student.headers.EmbeddableElement

interface ImportFunctionElement : EmbeddableElement<ByteArray> {
    val size: DWord

    override fun <T> getCopy(newData: T): ImportFunctionElement =
        super.getCopy(newData) as ImportFunctionElement

    override fun getDataType(): String = "ByteArray" // ByteArray, known at compile time
}