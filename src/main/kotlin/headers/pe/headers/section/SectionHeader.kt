package std.student.headers.pe.headers.section

import std.student.headers.pe.headers.section.elements.*
import java.io.RandomAccessFile

data class SectionHeader(
    val name: Name,
    val virtualSize: VirtualSize,
    val virtualAddress: VirtualAddress,
    val sizeOfRawData: SizeOfRawData,
    val pointerToRawData: PointerToRawData,
    val pointerToRelocations: PointerToRelocations,
    val pointerToLineNumbers: PointerToLineNumbers,
    val numberOfRelocations: NumberOfRelocations,
    val numberOfLineNumbers: NumberOfLineNumbers,
    val characteristics: Characteristics,
) {
    companion object {
        const val SIZE = 40

        fun get(headerOffset: Long, file: RandomAccessFile) =
            SectionHeader(
                name = Name(headerOffset, file),
                virtualSize = VirtualSize(headerOffset, file),
                virtualAddress = VirtualAddress(headerOffset, file),
                sizeOfRawData = SizeOfRawData(headerOffset, file),
                pointerToRawData = PointerToRawData(headerOffset, file),
                pointerToRelocations = PointerToRelocations(headerOffset, file),
                pointerToLineNumbers = PointerToLineNumbers(headerOffset, file),
                numberOfRelocations = NumberOfRelocations(headerOffset, file),
                numberOfLineNumbers = NumberOfLineNumbers(headerOffset, file),
                characteristics = Characteristics(headerOffset, file),
            )
    }

    override fun toString(): String {
        return """
            |SECTION HEADER:
            |    ${name.realName}: ${name.hex} ($name)
            |    ${virtualSize.realName}: ${virtualSize.hex}
            |    ${virtualAddress.realName}: ${virtualAddress.hex}
            |    ${sizeOfRawData.realName}: ${sizeOfRawData.hex}
            |    ${pointerToRawData.realName}: ${pointerToRawData.hex}
            |    ${pointerToRelocations.realName}: ${pointerToRelocations.hex}
            |    ${pointerToLineNumbers.realName}: ${pointerToLineNumbers.hex}
            |    ${numberOfRelocations.realName}: ${numberOfRelocations.hex}
            |    ${numberOfLineNumbers.realName}: ${numberOfLineNumbers.hex}
            |    ${characteristics.realName}: ${characteristics.hex}
        """.trimMargin()
    }
}