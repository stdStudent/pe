package std.student.headers.pe.headers.optional.elements.standard

import std.student.conventions.QWord
import std.student.conventions.byte
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class MinorLinkerVersion: PeElement<Byte> {
    override val data: Byte
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 3,
        size = 1,
    )

    override val hex: String get() = data.hex
    override val realName = "Minor Linker Version"
    override val description = """
        The linker minor version number. 
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.byte
    }

    constructor(original: MinorLinkerVersion, newData: Byte) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.byte = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}