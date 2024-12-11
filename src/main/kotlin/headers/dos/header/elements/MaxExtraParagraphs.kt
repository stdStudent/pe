package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class MaxExtraParagraphs: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 12
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "Maximum Extra Paragraphs [e_maxalloc]"
    override val description: String get() = """
        Specifies the maximum number of extra paragraphs needed 
        to be allocated by the program before it begins execution. 
        This indicates ADDITIONAL memory over and above 
        that required by the load module and the value specified by MINALLOC. 
        If the request cannot be satisfied, the program is allocated as much memory as is available.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: MinExtraParagraphs, newData: Word) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            this.word = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}