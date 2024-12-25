package std.student.headers.pe.headers.optional

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.toHex
import std.student.headers.Header
import std.student.headers.pe.headers.optional.elements.dataDirectories.DataDirectoryElement
import std.student.headers.pe.headers.optional.elements.standard.*
import std.student.headers.pe.headers.optional.elements.windowsSpecific.*
import std.student.headers.pe.headers.optional.elements.dataDirectories.elements.*
import std.student.headers.pe.type.PeType.Companion.PE32_MAGIC
import std.student.headers.pe.type.PeType.Companion.PE32_PLUS_MAGIC
import std.student.headers.pe.type.PeType.Companion.ROM_IMAGE
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
    val exportTable: ExportTable,
    val importTable: ImportTable,
    val resourceTable: ResourceTable,
    val exceptionTable: ExceptionTable,
    val certificateTable: CertificateTable,
    val baseRelocationTable: BaseRelocationTable,
    val debug: Debug,
    val architecture: Architecture,
    val globalPointer: GlobalPointer,
    val tlsTable: TLSTable,
    val loadConfigurationTable: LoadConfigurationTable,
    val boundImport: BoundImport,
    val importAddressTable: ImportAddressTable,
    val delayImportDescriptor: DelayImportDescriptor,
    val clrRuntimeHeader: CLRRuntimeHeader,
    val reserved: Reserved,
): Header {
    companion object {
        const val PE32_SIZE = 224
        const val PE32_PLUS_SIZE = 240

        fun get(headerOffset: QWord, file: RandomAccessFile): OptionalHeader {
            val magic = Magic(headerOffset, file)
            if (magic.data !in listOf(PE32_MAGIC, PE32_PLUS_MAGIC))
                throw IllegalArgumentException(
                    "Invalid magic number ${magic.hex} at position ${magic.realOffset.toHex()}. " +
                    "Expected either ${PE32_MAGIC.hex} or ${PE32_PLUS_MAGIC.hex} " +
                    "(ROM files with magic ${ROM_IMAGE.hex} are not supported). " +
                    "Optional header cannot be parsed."
                )

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
                exportTable = ExportTable(headerOffset, magic.data, file),
                importTable = ImportTable(headerOffset, magic.data, file),
                resourceTable = ResourceTable(headerOffset, magic.data, file),
                exceptionTable = ExceptionTable(headerOffset, magic.data, file),
                certificateTable = CertificateTable(headerOffset, magic.data, file),
                baseRelocationTable = BaseRelocationTable(headerOffset, magic.data, file),
                debug = Debug(headerOffset, magic.data, file),
                architecture = Architecture(headerOffset, magic.data, file),
                globalPointer = GlobalPointer(headerOffset, magic.data, file),
                tlsTable = TLSTable(headerOffset, magic.data, file),
                loadConfigurationTable = LoadConfigurationTable(headerOffset, magic.data, file),
                boundImport = BoundImport(headerOffset, magic.data, file),
                importAddressTable = ImportAddressTable(headerOffset, magic.data, file),
                delayImportDescriptor = DelayImportDescriptor(headerOffset, magic.data, file),
                clrRuntimeHeader = CLRRuntimeHeader(headerOffset, magic.data, file),
                reserved = Reserved(headerOffset, magic.data, file),
            )
        }
    }

    inline val size: DWord
        get() = when (magic.data) {
            PE32_MAGIC -> PE32_SIZE
            PE32_PLUS_MAGIC -> PE32_PLUS_SIZE
            else -> throw IllegalArgumentException("Invalid magic number: ${magic.hex}")
        }

    private val DataDirectoryElement.readableProperties
        get() = """
            |
            |      RVA: $rvaOffsetHex,
            |      size: $rvaSizeHex bytes
        """.trimMargin()

    override val headerName = "Optional Header"

    override fun toString(): String {
        return """
            |$headerName:
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
            |    ${exportTable.realName}: ${exportTable.readableProperties}
            |    ${importTable.realName}: ${importTable.readableProperties}
            |    ${resourceTable.realName}: ${resourceTable.readableProperties}
            |    ${exceptionTable.realName}: ${exceptionTable.readableProperties}
            |    ${certificateTable.realName}: ${certificateTable.readableProperties}
            |    ${baseRelocationTable.realName}: ${baseRelocationTable.readableProperties}
            |    ${debug.realName}: ${debug.readableProperties}
            |    ${architecture.realName}: ${architecture.readableProperties}
            |    ${globalPointer.realName}: ${globalPointer.readableProperties}
            |    ${tlsTable.realName}: ${tlsTable.readableProperties}
            |    ${loadConfigurationTable.realName}: ${loadConfigurationTable.readableProperties}
            |    ${boundImport.realName}: ${boundImport.readableProperties}
            |    ${importAddressTable.realName}: ${importAddressTable.readableProperties}
            |    ${delayImportDescriptor.realName}: ${delayImportDescriptor.readableProperties}
            |    ${clrRuntimeHeader.realName}: ${clrRuntimeHeader.readableProperties}
            |    ${reserved.realName}: ${reserved.readableProperties}
        """.trimMargin()
    }
}