package std.student.headers.dos.stub.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class Message: DosElement<ByteArray> {
    override val data: ByteArray
    override val realOffset: QWord

    override val relativeOffset: QWord = 14
    override val size: DWord = 38

    override val hex: String get() = data.joinToString { it.hex }
    override val realName: String get() = "Message"
    override val description: String get() = """
        Specifies the message that will be displayed when the DOS Stub is executed.
        It is usually "This program cannot be run in DOS mode".
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.array()
    }

    constructor(original: Message, newData: ByteArray) {
        if (newData.size != size)
            throw IllegalArgumentException("$realName must be $size bytes long.")

        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            put(data)
            flip()
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }

    override fun toString() = String(data)
}