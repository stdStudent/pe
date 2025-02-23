package std.student.headers.importTable.idt

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.headers.EmbeddableElement

interface ImportDirectoryTableElement : EmbeddableElement<DWord> {
    val relativeOffset: QWord
    val size: DWord get() = 4

    override fun <T> getCopy(newData: T): ImportDirectoryTableElement =
        super.getCopy(newData) as ImportDirectoryTableElement

    override fun getDataType(): String = "Int" // DWord, known at compile time
}