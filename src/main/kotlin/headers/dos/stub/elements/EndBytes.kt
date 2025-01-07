package std.student.headers.dos.stub.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class EndBytes: DosElement<ByteArray> {
    override val data: ByteArray
    override val realOffset: QWord

    override val relativeOffset: QWord = 52
    override val size: DWord = 12

    override val hex: String get() = data.joinToString { it.hex }
    override val realName: String get() = "End Bytes"
    override val description: String get() = """
        The last 12 bytes of the DOS stub. 
        It usually contains some formatting symbols (usually `.`, `CR`, `CR`, `LF`) followed by `$`,
        indicating the end of the DOS Stub's message. The rest of the bytes are usually zeroed out.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.array()
    }

    constructor(original: EndBytes, newData: ByteArray) {
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