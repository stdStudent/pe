package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class FileAlignment: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 36,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "File Alignment"
    override val description = """
	    The alignment factor (in bytes) that is used to align the raw data of sections in the image file. 
        The value should be a power of 2 between 512 and 64 K, inclusive. The default is 512. 
        If the SectionAlignment is less than the architecture's page size, 
        then FileAlignment must match SectionAlignment. 
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: FileAlignment, newData: DWord) {
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