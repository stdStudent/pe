package std.student.headers.importTable.dll

import std.student.conventions.DWord
import std.student.headers.EmbeddableElement

interface DLLElement : EmbeddableElement<ByteArray> {
    val size: DWord

    override fun <T> getCopy(newData: T): DLLElement =
        super.getCopy(newData) as DLLElement

    override fun getDataType(): String = "ByteArray" // ByteArray, known at compile time
}