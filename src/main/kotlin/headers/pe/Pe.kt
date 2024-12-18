package std.student.headers.pe

import std.student.conventions.DWord
import std.student.conventions.toQWord
import std.student.headers.pe.headers.coff.CoffHeader
import std.student.headers.pe.headers.optional.OptionalHeader
import std.student.headers.pe.headers.section.SectionHeader
import std.student.headers.pe.signature.SignatureHeader
import java.io.RandomAccessFile

class Pe {
    val signatureHeader: SignatureHeader
    val coffHeader: CoffHeader
    val optionalHeader: OptionalHeader
    val sectionsHeaders: List<SectionHeader>

    constructor(startOffset: DWord, file: RandomAccessFile) {
        val signatureHeaderOffset = startOffset.toQWord()
        signatureHeader = SignatureHeader.get(signatureHeaderOffset, file)

        val coffHeaderOffset = signatureHeaderOffset + SignatureHeader.SIZE
        coffHeader = CoffHeader.get(coffHeaderOffset, file)

        val optionalHeaderOffset = coffHeaderOffset + CoffHeader.SIZE
        optionalHeader = OptionalHeader.get(optionalHeaderOffset, file)

        val numberOfSections = coffHeader.numberOfSections.data
        sectionsHeaders = (0 until numberOfSections).map { sectionIndex ->
            val sectionHeaderOffset = optionalHeaderOffset + optionalHeader.size + sectionIndex * SectionHeader.SIZE
            SectionHeader.get(sectionHeaderOffset, file)
        }
    }

    override fun toString(): String {
        return """
            |$signatureHeader
            |
            |$coffHeader
            |
            |$optionalHeader
            |
            |${sectionsHeaders.joinToString("\n\n")}
        """.trimMargin()
    }
}
