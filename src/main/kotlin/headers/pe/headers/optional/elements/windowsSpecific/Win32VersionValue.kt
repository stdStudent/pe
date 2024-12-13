package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class Win32VersionValue: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 52,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "Windows (x86) Version Value"
    override val description = """
        Reserved, must be zero.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: Win32VersionValue, newData: DWord) {
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