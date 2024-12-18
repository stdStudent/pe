package std.student.headers.pe.signature.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class PeSignature: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 0,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "PE Signature"
    override val description = """
        After the MS-DOS stub, at the file offset specified at offset 0x3c, 
        is a 4-byte signature that identifies the file as a PE format image file. 
        This signature is "PE\0\0" (the letters "P" and "E" followed by two null bytes).
    """.trimIndent()

    constructor(offset: QWord, file: RandomAccessFile) {
        realOffset = offset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: PeSignature, newData: DWord) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.dword = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}