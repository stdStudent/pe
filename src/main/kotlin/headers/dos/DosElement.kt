package std.student.headers.dos

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.headers.EmbeddableElement

interface DosElement<T>: EmbeddableElement<T> {
    val relativeOffset: QWord
    val size: DWord
}