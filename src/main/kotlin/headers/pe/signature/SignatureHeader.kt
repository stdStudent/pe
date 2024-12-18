package std.student.headers.pe.signature

import std.student.headers.Header
import std.student.headers.pe.signature.elements.*
import java.io.RandomAccessFile

data class SignatureHeader(
    val peSignature: PeSignature,
): Header {
    companion object {
        const val SIZE = 4

        fun get(headerOffset: Long, file: RandomAccessFile) =
            SignatureHeader(
                peSignature = PeSignature(headerOffset, file)
            )
    }

    override val headerName = "Signature Header"

    override fun toString(): String {
        return """
            |$headerName:
            |   ${peSignature.realName}: ${peSignature.hex}
        """.trimMargin()
    }
}
