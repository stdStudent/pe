package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class CheckSum: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 64,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "Checksum"
    override val description = """
        The image file checksum. The algorithm for computing the checksum is incorporated into IMAGHELP.DLL. 
        The following are checked for validation at load time: 
        all drivers, any DLL loaded at boot time, and any DLL that is loaded into a critical Windows process. 
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: CheckSum, newData: DWord) {
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