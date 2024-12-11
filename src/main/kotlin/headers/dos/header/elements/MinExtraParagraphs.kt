package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class MinExtraParagraphs: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 10
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "Minimum Extra Paragraphs [e_minalloc]"
    override val description: String get() = """
        Specifies the minimum number of extra paragraphs needed to be allocated to begin execution. 
        This is IN ADDITION to the memory required to hold the load module. 
        This value normally represents the total size of any uninitialised data 
        and/or stack segments that are linked at the end of a program. 
        This space is not directly included in the load module, 
        since there are no particular initializing values and it would simply waste disk space.
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