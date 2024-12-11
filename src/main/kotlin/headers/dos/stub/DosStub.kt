package std.student.headers.dos.stub

import std.student.headers.dos.stub.elements.*
import java.io.RandomAccessFile

data class DosStub(
    val startBytes: StartBytes,
    val message: Message,
    val endBytes: EndBytes,
) {
    companion object {
        const val HEADER_OFFSET = 0x40L

        fun get(file: RandomAccessFile): DosStub {
            return DosStub(
                startBytes = StartBytes(HEADER_OFFSET, file),
                message = Message(HEADER_OFFSET, file),
                endBytes = EndBytes(HEADER_OFFSET, file),
            )
        }
    }

    override fun toString(): String {
        return """
            |DOS STUB:
            |    ${startBytes.realName}: ${startBytes.hex}
            |    ${message.realName}: ${message.hex} (${message})
            |    ${endBytes.realName}: ${endBytes.hex}
        """.trimMargin()
    }
}