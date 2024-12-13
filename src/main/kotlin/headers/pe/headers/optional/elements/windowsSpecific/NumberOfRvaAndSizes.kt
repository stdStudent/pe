package std.student.headers.pe.headers.optional.elements.windowsSpecific

import std.student.conventions.*
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.headers.pe.type.PeType.Companion.PE32_MAGIC
import std.student.headers.pe.type.PeType.Companion.PE32_PLUS_MAGIC
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class NumberOfRvaAndSizes: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType: PeType

    override val hex: String get() = data.hex
    override val realName = "Number of RVA and Sizes"
    override val description = """
	    The number of data-directory entries in the remainder of the optional header. 
        Each describes a location and size. 
    """.trimIndent()

    constructor(headerOffset: QWord, magic: Word, file: RandomAccessFile) {
        peType = when (magic) {
            PE32_MAGIC -> PeType.Pe32(92, 4)
            PE32_PLUS_MAGIC -> PeType.Pe32Plus(108, 4)
            else -> throw IllegalArgumentException("Invalid magic number: ${magic.hex}")
        }

        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)

        data = buffer.dword
    }

    constructor(original: NumberOfRvaAndSizes, newData: DWord) {
        data = newData
        realOffset = original.realOffset
        peType = original.peType
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.dword = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}