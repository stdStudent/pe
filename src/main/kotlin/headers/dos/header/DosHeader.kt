package std.student.headers.dos.header

import std.student.headers.dos.header.elements.*
import java.io.RandomAccessFile

data class DosHeader(
    val mZ: Mz,
    val lastSize: LastSize,
    val pagesInFile: PagesInFile,
    val relocations: Relocations,
    val headerSizeInParagraphs: HeaderSizeInParagraphs,
    val minExtraParagraphs: MinExtraParagraphs,
    val maxExtraParagraphs: MaxExtraParagraphs,
    val initialRelativeSS: InitialRelativeSS,
    val initialRelativeSP: InitialRelativeSP,
    val checksum: Checksum,
    val initialRelativeIP: InitialRelativeIP,
    val initialRelativeCS: InitialRelativeCS,
    val fileAddOfRelocationTable: FileAddOfRelocationTable,
    val overlayNumber: OverlayNumber,
    val firstReserved: FirstReserved,
    val oemIdentifier: OemIdentifier,
    val oemInformation: OemInformation,
    val secondReserved: SecondReserved,
    val offsetToPeSignature: OffsetToPeSignature,
) {
    companion object {
        const val HEADER_OFFSET = 0x0L

        fun get(file: RandomAccessFile): DosHeader {
            return DosHeader(
                mZ = Mz(HEADER_OFFSET, file),
                lastSize = LastSize(HEADER_OFFSET, file),
                pagesInFile = PagesInFile(HEADER_OFFSET, file),
                relocations = Relocations(HEADER_OFFSET, file),
                headerSizeInParagraphs = HeaderSizeInParagraphs(HEADER_OFFSET, file),
                minExtraParagraphs = MinExtraParagraphs(HEADER_OFFSET, file),
                maxExtraParagraphs = MaxExtraParagraphs(HEADER_OFFSET, file),
                initialRelativeSS = InitialRelativeSS(HEADER_OFFSET, file),
                initialRelativeSP = InitialRelativeSP(HEADER_OFFSET, file),
                checksum = Checksum(HEADER_OFFSET, file),
                initialRelativeIP = InitialRelativeIP(HEADER_OFFSET, file),
                initialRelativeCS = InitialRelativeCS(HEADER_OFFSET, file),
                fileAddOfRelocationTable = FileAddOfRelocationTable(HEADER_OFFSET, file),
                overlayNumber = OverlayNumber(HEADER_OFFSET, file),
                firstReserved = FirstReserved(HEADER_OFFSET, file),
                oemIdentifier = OemIdentifier(HEADER_OFFSET, file),
                oemInformation = OemInformation(HEADER_OFFSET, file),
                secondReserved = SecondReserved(HEADER_OFFSET, file),
                offsetToPeSignature = OffsetToPeSignature(HEADER_OFFSET, file),
            )
        }
    }

    override fun toString(): String {
        return """
            |DOS HEADER:
            |    ${mZ.realName}: ${mZ.hex}
            |    ${lastSize.realName}: ${lastSize.hex}
            |    ${pagesInFile.realName}: ${pagesInFile.hex}
            |    ${relocations.realName}: ${relocations.hex}
            |    ${headerSizeInParagraphs.realName}: ${headerSizeInParagraphs.hex}
            |    ${minExtraParagraphs.realName}: ${minExtraParagraphs.hex}
            |    ${maxExtraParagraphs.realName}: ${maxExtraParagraphs.hex}
            |    ${initialRelativeSS.realName}: ${initialRelativeSS.hex}
            |    ${initialRelativeSP.realName}: ${initialRelativeSP.hex}
            |    ${checksum.realName}: ${checksum.hex}
            |    ${initialRelativeIP.realName}: ${initialRelativeIP.hex}
            |    ${initialRelativeCS.realName}: ${initialRelativeCS.hex}
            |    ${fileAddOfRelocationTable.realName}: ${fileAddOfRelocationTable.hex}
            |    ${overlayNumber.realName}: ${overlayNumber.hex}
            |    ${firstReserved.realName}: ${firstReserved.hex}
            |    ${oemIdentifier.realName}: ${oemIdentifier.hex}
            |    ${oemInformation.realName}: ${oemInformation.hex}
            |    ${secondReserved.realName}: ${secondReserved.hex}
            |    ${offsetToPeSignature.realName}: ${offsetToPeSignature.hex}
        """.trimMargin()
    }
}