package std.student.headers.pe

import std.student.conventions.*
import std.student.headers.pe.OptionalHeader.Companion.PE32_MAGIC
import std.student.headers.pe.OptionalHeader.Companion.PE32_PLUS_MAGIC
import java.nio.ByteBuffer

data class StandardFields(
    val magic: Word,
    val majorLinkerVersion: Byte,
    val minorLinkerVersion: Byte,
    val sizeOfCode: DWord,
    val sizeOfInitializedData: DWord,
    val sizeOfUninitializedData: DWord,
    val addressOfEntryPoint: DWord,
    val baseOfCode: DWord,
    val baseOfData: DWord?,
) {
    companion object {
        const val PE32_SIZE = 28
        const val PE32_PLUS_SIZE = 24 // PE32+ has no BaseOfData field

        fun get(buffer: ByteBuffer): StandardFields {
            val magic = buffer.word

            return StandardFields(
                magic = magic,
                majorLinkerVersion = buffer.byte,
                minorLinkerVersion = buffer.byte,
                sizeOfCode = buffer.dword,
                sizeOfInitializedData = buffer.dword,
                sizeOfUninitializedData = buffer.dword,
                addressOfEntryPoint = buffer.dword,
                baseOfCode = buffer.dword,
                baseOfData = if (magic == PE32_MAGIC) buffer.dword else null
            )
        }
    }

    fun isPE32(): Boolean = magic == PE32_MAGIC
    fun isPE32Plus(): Boolean = magic == PE32_PLUS_MAGIC

    override fun toString(): String {
        val representation = """
            |STANDARD FIELDS:
            |   magic: ${magic.hex}
            |   major linker version: ${majorLinkerVersion.hex}
            |   minor linker version: ${minorLinkerVersion.hex}
            |   size of code: ${sizeOfCode.hex}
            |   size of initialized data: ${sizeOfInitializedData.hex}
            |   size of uninitialized data: ${sizeOfUninitializedData.hex}
            |   address of entry point: ${addressOfEntryPoint.hex}
            |   base of code: ${baseOfCode.hex}
        """.trimMargin()

        return if (isPE32())
            representation + "\n   base of data: ${baseOfData!!.hex}"
        else
            representation
    }
}
