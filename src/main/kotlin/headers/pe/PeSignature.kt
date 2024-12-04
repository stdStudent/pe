package std.student.headers.pe

import std.student.conventions.DWord
import std.student.conventions.dword
import std.student.conventions.hex
import java.nio.ByteBuffer

data class PeSignature(
    val signature: DWord,
) {
    companion object {
        const val SIZE = 4

        fun get(buffer: ByteBuffer) =
            PeSignature(
                signature = buffer.dword
            )
    }

    override fun toString(): String {
        return """
            |PE SIGNATURE:
            |   signature: ${signature.hex}
        """.trimMargin()
    }
}
