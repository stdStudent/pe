package std.student.headers.pe.headers.optional.elements.standard

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.headers.pe.type.PeType.Companion.PE32_MAGIC
import std.student.headers.pe.type.PeType.Companion.PE32_PLUS_MAGIC
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class BaseOfData: PeElement<Option<DWord>> {
    override val data: Option<DWord>
    override val realOffset: QWord
    override val peType: PeType

    override val hex: String get() = data.fold({ "Absent" }, { it.hex })
    override val realName = "Base of Data"
    override val description = """
        The address that is relative to the image base of the beginning-of-data section when it is loaded into memory.
    """.trimIndent()

    constructor(headerOffset: QWord, magic: Word, file: RandomAccessFile) {
        when (magic) {
            PE32_MAGIC -> {
                peType = PeType.Pe32(24, 4)
                realOffset = headerOffset + peType.relativeOffset
                val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
                data = Some(buffer.dword)
            }

            PE32_PLUS_MAGIC -> {
                peType = PeType.Pe32Plus(-1, -1)
                realOffset = -1
                data = None
            }

            else -> throw IllegalArgumentException("Invalid magic number")
        }
    }

    constructor(original: BaseOfData, newData: DWord) {
        if (original.peType is PeType.Pe32Plus)
            throw IllegalArgumentException("Cannot change BaseOfData for PE32+")

        data = Some(newData)
        realOffset = original.realOffset
        peType = original.peType
    }

    override fun embed(file: RandomAccessFile) {
        if (peType is PeType.Pe32Plus)
            throw IllegalStateException("Cannot change BaseOfData for PE32+")

        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.dword = data.fold({ 0 }, { it })
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}