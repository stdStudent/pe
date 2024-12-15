package std.student.headers.pe

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.toQWord
import std.student.headers.pe.headers.coff.CoffHeader
import std.student.headers.pe.headers.optional.OptionalHeader
import std.student.headers.pe.headers.section.SectionHeader
import std.student.headers.pe.signature.PeSignature
import java.io.RandomAccessFile

class Pe {
    private val peSignatureOffset: QWord
    private val peSignature: PeSignature

    private val coffHeaderOffset: QWord
    private val coffHeader: CoffHeader

    private val optionalHeaderOffset: QWord
    private val optionalHeader: OptionalHeader

    private val sectionsHeaders: List<SectionHeader>

    constructor(startOffset: DWord, file: RandomAccessFile) {
        peSignatureOffset = startOffset.toQWord()
        peSignature = PeSignature(peSignatureOffset, file)

        coffHeaderOffset = peSignatureOffset + peSignature.peType.size
        coffHeader = CoffHeader.get(coffHeaderOffset, file)

        optionalHeaderOffset = coffHeaderOffset + CoffHeader.SIZE
        optionalHeader = OptionalHeader.get(optionalHeaderOffset, file)

        val numberOfSections = coffHeader.numberOfSections.data
        sectionsHeaders = (0 until numberOfSections).map { sectionIndex ->
            val sectionHeaderOffset = optionalHeaderOffset + optionalHeader.size + sectionIndex * SectionHeader.SIZE
            SectionHeader.get(sectionHeaderOffset, file)
        }
    }

    constructor(
        startOffset: QWord,
        peSignature: PeSignature,
        coffHeader: CoffHeader,
        optionalHeader: OptionalHeader,
        sectionHeaders: List<SectionHeader>,
    ) {
        peSignatureOffset = startOffset
        this.peSignature = peSignature

        coffHeaderOffset = peSignatureOffset + peSignature.peType.size
        this.coffHeader = coffHeader

        optionalHeaderOffset = coffHeaderOffset + CoffHeader.SIZE
        this.optionalHeader = optionalHeader

        sectionsHeaders = sectionHeaders
    }

    override fun toString(): String {
        return """
            |$peSignature
            |
            |$coffHeader
            |
            |$optionalHeader
            |
            |${sectionsHeaders.joinToString("\n\n")}
        """.trimMargin()
    }
}
