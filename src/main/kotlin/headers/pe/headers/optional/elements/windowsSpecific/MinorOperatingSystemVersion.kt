package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class MinorOperatingSystemVersion: PeElement<Word> {
    override val data: Word
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 42,
        size = 2,
    )

    override val hex: String get() = data.hex
    override val realName = "Minor Operating System Version"
    override val description = """
        The minor version number of the required operating system.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.word
    }

    constructor(original: MinorOperatingSystemVersion, newData: Word) {
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