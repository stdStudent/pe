package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class MajorImageVersion: PeElement<Word> {
    override val data: Word
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 44,
        size = 2,
    )

    override val hex: String get() = data.hex
    override val realName = "Major Image Version"
    override val description = """
        The major version number of the image.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.word
    }

    constructor(original: MajorImageVersion, newData: Word) {
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