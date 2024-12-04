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

    // TODO: Add optional header and section headers

    constructor(startOffset: DWord, file: RandomAccessFile) {
        peSignatureOffset = startOffset
        val peSignatureBuffer = BufferUtils.getBuffer(file, peSignatureOffset, PeSignature.SIZE, ByteOrder.LITTLE_ENDIAN)
        peSignature = PeSignature.get(peSignatureBuffer)

        coffHeaderOffset = peSignatureOffset + PeSignature.SIZE
        val coffHeaderBuffer = BufferUtils.getBuffer(file, coffHeaderOffset, CoffHeader.SIZE, ByteOrder.LITTLE_ENDIAN)
        coffHeader = CoffHeader.get(coffHeaderBuffer)
    }

    constructor(startOffset: DWord, peSignature: PeSignature, coffHeader: CoffHeader) {
        peSignatureOffset = startOffset
        this.peSignature = peSignature

        coffHeaderOffset = peSignatureOffset + PeSignature.SIZE
        this.coffHeader = coffHeader
    }

    fun getPeSignature() = peSignature.copy()
    fun getCoffHeader() = coffHeader.copy()

    override fun toString(): String {
        return """
            |$peSignature
            |
            |$coffHeader
        """.trimMargin()
    }
}
