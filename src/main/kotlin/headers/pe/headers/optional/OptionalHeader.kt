package std.student.headers.pe.headers.optional

import std.student.conventions.QWord
import std.student.headers.pe.headers.optional.elements.standard.*
import java.io.RandomAccessFile

data class OptionalHeader(
    // Standard fields
    val magic: Magic,
    val majorLinkerVersion: MajorLinkerVersion,
    val minorLinkerVersion: MinorLinkerVersion,
    val sizeOfCode: SizeOfCode,
    val sizeOfInitializedData: SizeOfInitializedData,
    val sizeOfUninitializedData: SizeOfUninitializedData,
    val addressOfEntryPoint: AddressOfEntryPoint,
    val baseOfCode: BaseOfCode,
    val baseOfData: BaseOfData,

    // Windows-specific fields

    // Data directories

) {
    companion object {
        const val PE32_SIZE = 216
        const val PE32_PLUS_SIZE = 224

        fun get(headerOffset: QWord, file: RandomAccessFile): OptionalHeader {
            val magic = Magic(headerOffset, file)

            return OptionalHeader(
                // Standard fields
                magic = magic,
                majorLinkerVersion = MajorLinkerVersion(headerOffset, file),
                minorLinkerVersion = MinorLinkerVersion(headerOffset, file),
                sizeOfCode = SizeOfCode(headerOffset, file),
                sizeOfInitializedData = SizeOfInitializedData(headerOffset, file),
                sizeOfUninitializedData = SizeOfUninitializedData(headerOffset, file),
                addressOfEntryPoint = AddressOfEntryPoint(headerOffset, file),
                baseOfCode = BaseOfCode(headerOffset, file),
                baseOfData = BaseOfData(headerOffset, magic.data, file),

                // Windows-specific fields

                // Data directories
            )
        }
    }

    override fun toString(): String {
        return """
            |OPTIONAL HEADER:
            |  (Standard fields)
            |    ${magic.realName}: ${magic.hex}
            |    ${majorLinkerVersion.realName}: ${majorLinkerVersion.hex}
            |    ${minorLinkerVersion.realName}: ${minorLinkerVersion.hex}
            |    ${sizeOfCode.realName}: ${sizeOfCode.hex}
            |    ${sizeOfInitializedData.realName}: ${sizeOfInitializedData.hex}
            |    ${sizeOfUninitializedData.realName}: ${sizeOfUninitializedData.hex}
            |    ${addressOfEntryPoint.realName}: ${addressOfEntryPoint.hex}
            |    ${baseOfCode.realName}: ${baseOfCode.hex}
            |    ${baseOfData.realName}: ${baseOfData.hex}
            |
            |  (Windows-specific fields)
            |
            |  (Data directories)
        """.trimMargin()
    }
}