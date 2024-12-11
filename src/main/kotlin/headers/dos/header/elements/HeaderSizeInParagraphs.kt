package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class HeaderSizeInParagraphs: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 8
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "Header Size In Paragraphs [e_cparhdr]"
    override val description: String get() = """
        Specifies the size of the executable header in terms of paragraphs (16 byte chunks). 
        It indicates the offset of the program's compiled/assembled 
        and linked image (the load module) within the executable file. 
        The size of the load module can be deduced by subtracting this value (converted to bytes) 
        from the overall file size derived from combining the e_cp (number of file pages) 
        and e_cblp (number of bytes in last page) values. 
        The header always spans an even number of paragraphs.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: HeaderSizeInParagraphs, newData: Word) {
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