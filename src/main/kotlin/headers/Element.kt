package std.student.headers

import std.student.conventions.QWord
import java.io.RandomAccessFile

interface Element<T> {
    val data: T
    val realOffset: QWord

    val hex: String
    val realName: String
    val description: String
}

interface EmbeddableElement<T>: Element<T> {
    fun embed(file: RandomAccessFile)
}
