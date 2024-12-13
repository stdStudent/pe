package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class SizeOfHeaders: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 60,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "Size of Headers"
    override val description = """
        The combined size of an MS-DOS stub, PE header, and section headers rounded up to a multiple of FileAlignment. 
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: SizeOfHeaders, newData: DWord) {
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