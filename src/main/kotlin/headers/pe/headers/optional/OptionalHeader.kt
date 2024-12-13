package std.student.headers.pe.headers.optional

import std.student.conventions.QWord
import std.student.headers.pe.headers.optional.elements.standard.*
import std.student.headers.pe.headers.optional.elements.windowsSpecific.*
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
    val imageBase: ImageBase,
    val sectionAlignment: SectionAlignment,
    val fileAlignment: FileAlignment,
    val majorOperatingSystemVersion: MajorOperatingSystemVersion,
    val minorOperatingSystemVersion: MinorOperatingSystemVersion,
    val majorImageVersion: MajorImageVersion,
    val minorImageVersion: MinorImageVersion,
    val majorSubsystemVersion: MajorSubsystemVersion,
    val minorSubsystemVersion: MinorSubsystemVersion,
    val win32VersionValue: Win32VersionValue,
    val sizeOfImage: SizeOfImage,
    val sizeOfHeaders: SizeOfHeaders,
    val checkSum: CheckSum,
    val subsystem: Subsystem,
    val dllCharacteristics: DllCharacteristics,
    val sizeOfStackReserve: SizeOfStackReserve,
    val sizeOfStackCommit: SizeOfStackCommit,
    val sizeOfHeapReserve: SizeOfHeapReserve,
    val sizeOfHeapCommit: SizeOfHeapCommit,
    val loaderFlags: LoaderFlags,
    val numberOfRvaAndSizes: NumberOfRvaAndSizes,


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
                imageBase = ImageBase(headerOffset, magic.data, file),
                sectionAlignment = SectionAlignment(headerOffset, file),
                fileAlignment = FileAlignment(headerOffset, file),
                majorOperatingSystemVersion = MajorOperatingSystemVersion(headerOffset, file),
                minorOperatingSystemVersion = MinorOperatingSystemVersion(headerOffset, file),
                majorImageVersion = MajorImageVersion(headerOffset, file),
                minorImageVersion = MinorImageVersion(headerOffset, file),
                majorSubsystemVersion = MajorSubsystemVersion(headerOffset, file),
                minorSubsystemVersion = MinorSubsystemVersion(headerOffset, file),
                win32VersionValue = Win32VersionValue(headerOffset, file),
                sizeOfImage = SizeOfImage(headerOffset, file),
                sizeOfHeaders = SizeOfHeaders(headerOffset, file),
                checkSum = CheckSum(headerOffset, file),
                subsystem = Subsystem(headerOffset, file),
                dllCharacteristics = DllCharacteristics(headerOffset, file),
                sizeOfStackReserve = SizeOfStackReserve(headerOffset, magic.data, file),
                sizeOfStackCommit = SizeOfStackCommit(headerOffset, magic.data, file),
                sizeOfHeapReserve = SizeOfHeapReserve(headerOffset, magic.data, file),
                sizeOfHeapCommit = SizeOfHeapCommit(headerOffset, magic.data, file),
                loaderFlags = LoaderFlags(headerOffset, magic.data, file),
                numberOfRvaAndSizes = NumberOfRvaAndSizes(headerOffset, magic.data, file),

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
            |    ${imageBase.realName}: ${imageBase.hex}
            |    ${sectionAlignment.realName}: ${sectionAlignment.hex}
            |    ${fileAlignment.realName}: ${fileAlignment.hex}
            |    ${majorOperatingSystemVersion.realName}: ${majorOperatingSystemVersion.hex}
            |    ${minorOperatingSystemVersion.realName}: ${minorOperatingSystemVersion.hex}
            |    ${majorImageVersion.realName}: ${majorImageVersion.hex}
            |    ${minorImageVersion.realName}: ${minorImageVersion.hex}
            |    ${majorSubsystemVersion.realName}: ${majorSubsystemVersion.hex}
            |    ${minorSubsystemVersion.realName}: ${minorSubsystemVersion.hex}
            |    ${win32VersionValue.realName}: ${win32VersionValue.hex}
            |    ${sizeOfImage.realName}: ${sizeOfImage.hex}
            |    ${sizeOfHeaders.realName}: ${sizeOfHeaders.hex}
            |    ${checkSum.realName}: ${checkSum.hex}
            |    ${subsystem.realName}: ${subsystem.hex}
            |    ${dllCharacteristics.realName}: ${dllCharacteristics.hex}
            |    ${sizeOfStackReserve.realName}: ${sizeOfStackReserve.hex}
            |    ${sizeOfStackCommit.realName}: ${sizeOfStackCommit.hex}
            |    ${sizeOfHeapReserve.realName}: ${sizeOfHeapReserve.hex}
            |    ${sizeOfHeapCommit.realName}: ${sizeOfHeapCommit.hex}
            |    ${loaderFlags.realName}: ${loaderFlags.hex}
            |    ${numberOfRvaAndSizes.realName}: ${numberOfRvaAndSizes.hex}
            |
            |  (Data directories)
        """.trimMargin()
    }
}