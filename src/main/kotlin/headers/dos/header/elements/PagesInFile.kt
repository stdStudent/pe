package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class PagesInFile: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 4
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "Pages In File [e_cp]"
    override val description: String get() = """
        Specifies the number of pages required to hold the file. 
        For example, if the file contains 1024 bytes, 
        and we assume the file has pages of a size of 512 bytes, 
        this word would contain 0x0002; 
        if the file contains 1025 bytes, this word would contain 0x0003.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: PagesInFile, newData: Word) {
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