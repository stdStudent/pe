package std.student.headers.dos.header.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class OffsetToPeSignature: DosElement<DWord> {
    override val data: DWord
    override val realOffset: QWord

    override val relativeOffset: QWord = 60
    override val size: DWord = 4

    override val hex: String get() = data.hex
    override val realName: String get() = "Offset to PE signature [e_lfanew]"
    override val description: String get() = """
        Specifies the file address of the of the new exe header. 
        In particular, it is a 4-byte offset into the file where the PE file header is located. 
        It is necessary to use this offset to locate the PE header in the file.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.dword
    }

    constructor(original: OffsetToPeSignature, newData: DWord) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            this.dword = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}