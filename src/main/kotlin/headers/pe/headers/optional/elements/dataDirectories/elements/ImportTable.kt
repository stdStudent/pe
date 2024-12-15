package std.student.headers.pe.headers.optional.elements.dataDirectories.elements

import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.hex
import std.student.conventions.qword
import std.student.headers.pe.headers.optional.elements.dataDirectories.DataDirectoryElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class ImportTable: DataDirectoryElement {
    override val data: QWord
    override val realOffset: QWord
    override val peType: PeType

    override val hex: String get() = data.hex
    override val realName = "Import Table [.idata]"
    override val description = """
	    The import table address and size.
    """.trimIndent()

    constructor(headerOffset: QWord, magic: Word, file: RandomAccessFile) {
        val size = 8

        peType = when (magic) {
            PeType.Companion.PE32_MAGIC -> PeType.Pe32(104, size)
            PeType.Companion.PE32_PLUS_MAGIC -> PeType.Pe32Plus(120, size)
            else -> throw IllegalArgumentException("Invalid magic number: ${magic.hex}")
        }

        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)

        data = buffer.qword
    }

    constructor(original: ImportTable, newData: QWord) {
        data = newData
        realOffset = original.realOffset
        peType = original.peType
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.qword = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}
