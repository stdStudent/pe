package std.student.headers.dos.header.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class LastSize: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 2
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName = "Last Size [e_cblp]"
    override val description = """
        Specifies the number of bytes actually used in the last page, 
        with the special case of a full page being represented 
        by a value of zero (since the last page is never empty). 
        For example, assuming a page size of 512 bytes, 
        this value would be 0x0000 for a 1024 byte file, 
        and 0x0001 for a 1025 byte file 
        (since it only contains one valid byte).
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(lastSize: LastSize, newData: Word) {
        data = newData
        realOffset = lastSize.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            this.word = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}