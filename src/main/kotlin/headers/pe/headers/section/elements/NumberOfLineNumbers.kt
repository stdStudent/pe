package std.student.headers.pe.headers.section.elements

import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class NumberOfLineNumbers: PeElement<Word> {
    override val data: Word
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 34,
        size = 2,
    )

    override val hex: String get() = data.hex
    override val realName = "Number of Line Numbers"
    override val description = """
        The number of line-number entries for the section. 
        This value should be zero for an image because COFF debugging information is deprecated.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.word
    }

    constructor(original: NumberOfLineNumbers, newData: Word) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.word = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}