package std.student.headers.dos.stub.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class StartBytes: DosElement<ByteArray> {
    override val data: ByteArray
    override val realOffset: QWord

    override val relativeOffset: QWord = 0
    override val size: DWord = 14

    override val hex: String get() = data.joinToString { it.hex }
    override val realName: String get() = "Start Bytes"
    override val description: String get() = """
        The first 14 bytes of the DOS stub.
        It usually contains some commands for printing out the DOS Stub message.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.array()
    }

    constructor(original: StartBytes, newData: ByteArray) {
        if (newData.size > size)
            throw IllegalArgumentException("$realName must be no more than $size bytes long.")

        val remainingZeroes = if (newData.size < size) {
            val diff = size - newData.size
            ByteArray(diff) { 0 }
        } else
            byteArrayOf()

        data = newData + remainingZeroes
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
}