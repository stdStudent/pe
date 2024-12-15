package std.student.headers.pe.headers.optional.elements.dataDirectories

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.toDWord
import std.student.headers.pe.PeElement

interface DataDirectoryElement: PeElement<QWord> {
    val rvaOffset: DWord get() = data.and(0xFFFFFFFF).toDWord()
    val rvaSize: DWord get() = data.shr(32).toDWord()

    val rvaOffsetHex: String get() = rvaOffset.hex
    val rvaSizeHex: String get() = rvaSize.hex
}