package std.student.headers.dos

import std.student.conventions.hex
import std.student.utils.BufferUtils
import java.io.RandomAccessFile
import java.nio.ByteOrder

data class DosStub(
    val startBytes: List<Byte> = List(START_BYTES_SIZE) { 0 },
    val message: List<Byte> = List(MESSAGE_SIZE) { 0 },
    val endBytes: List<Byte> = List(END_BYTES_SIZE) { 0 },
) {
    companion object {
        private const val START_BYTES_SIZE = 14
        private const val MESSAGE_SIZE = 38
        private const val END_BYTES_SIZE = 12

        const val START_OFFSET = 0x40L
        const val SIZE = 64

        fun get(file: RandomAccessFile): DosStub {
            val buffer = BufferUtils.getBuffer(file, START_OFFSET, SIZE, ByteOrder.LITTLE_ENDIAN)
            return DosStub(
                startBytes = List(START_BYTES_SIZE) { buffer.get() },
                message = List(MESSAGE_SIZE) { buffer.get() },
                endBytes = List(END_BYTES_SIZE) { buffer.get() }
            )
        }
    }

    override fun toString(): String {
        return """
            |DOS STUB:
            |   startBytes: ${startBytes.joinToString { it.hex }}
            |   message: ${message.joinToString("") { it.toInt().toChar().toString() }}
            |   endBytes: ${endBytes.joinToString { it.hex }}
        """.trimMargin()
    }
}
