package std.student.headers.pe

import std.student.conventions.DWord
import std.student.utils.BufferUtils
import java.io.RandomAccessFile
import java.nio.ByteOrder

class PeHeader {
    private val peSignatureOffset: DWord
    private val peSignature: PeSignature

    private val coffHeaderOffset: DWord
    private val coffHeader: CoffHeader

    private val optionalHeaderOffset: DWord
    private val optionalHeader: OptionalHeader

    // TODO: Add optional header and section headers

    constructor(startOffset: DWord, file: RandomAccessFile) {
        peSignatureOffset = startOffset
        val peSignatureBuffer = BufferUtils.getBuffer(file, peSignatureOffset, PeSignature.SIZE, ByteOrder.LITTLE_ENDIAN)
        peSignature = PeSignature.get(peSignatureBuffer)

        coffHeaderOffset = peSignatureOffset + PeSignature.SIZE
        val coffHeaderBuffer = BufferUtils.getBuffer(file, coffHeaderOffset, CoffHeader.SIZE, ByteOrder.LITTLE_ENDIAN)
        coffHeader = CoffHeader.get(coffHeaderBuffer)

        optionalHeaderOffset = coffHeaderOffset + CoffHeader.SIZE
        optionalHeader = OptionalHeader(optionalHeaderOffset, file)
    }

    constructor(
        startOffset: DWord,
        peSignature: PeSignature,
        coffHeader: CoffHeader,
        optionalHeader: OptionalHeader,
    ) {
        peSignatureOffset = startOffset
        this.peSignature = peSignature

        coffHeaderOffset = peSignatureOffset + PeSignature.SIZE
        this.coffHeader = coffHeader

        optionalHeaderOffset = coffHeaderOffset + CoffHeader.SIZE
        this.optionalHeader = optionalHeader
    }

    override fun toString(): String {
        return """
            |$peSignature
            |
            |$coffHeader
            |
            |$optionalHeader
        """.trimMargin()
    }
}
