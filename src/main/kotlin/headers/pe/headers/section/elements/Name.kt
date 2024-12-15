package std.student.headers.pe.headers.section.elements

import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile
import java.nio.ByteBuffer.wrap

class Name: PeElement<ByteArray> {
    override val data: ByteArray
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 0,
        size = 8,
    )

    override val hex: String get() = data.joinToString { it.hex }
    override val realName = "Name"
    override val description = """
        An 8-byte, null-padded UTF-8 encoded string. 
        If the string is exactly 8 characters long, there is no terminating null. 
        For longer names, this field contains a slash (/) that is followed by 
        an ASCII representation of a decimal number that is an offset into the string table. 
        Executable images do not use a string table and do not support section names longer than 8 characters. 
        Long names in object files are truncated if they are emitted to an executable file.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.array()
    }

    constructor(original: Name, newData: ByteArray) {
        if (newData.size != peType.size)
            throw IllegalArgumentException("$realName must be ${peType.size} bytes long.")

        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            wrap(data)
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }

    override fun toString() = String(data).trimEnd { it == 0.toChar() }
}