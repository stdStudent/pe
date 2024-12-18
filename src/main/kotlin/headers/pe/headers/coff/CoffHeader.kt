package std.student.headers.pe.headers.coff

import std.student.conventions.QWord
import std.student.headers.Header
import std.student.headers.pe.headers.coff.elements.Characteristics
import std.student.headers.pe.headers.coff.elements.NumberOfSections
import std.student.headers.pe.headers.coff.elements.NumberOfSymbols
import std.student.headers.pe.headers.coff.elements.PointerToSymbolTable
import std.student.headers.pe.headers.coff.elements.SizeOfOptionalHeader
import std.student.headers.pe.headers.coff.elements.TargetMachine
import std.student.headers.pe.headers.coff.elements.TimeDateStamp
import java.io.RandomAccessFile

data class CoffHeader(
    val targetMachine: TargetMachine,
    val numberOfSections: NumberOfSections,
    val timeDateStamp: TimeDateStamp,
    val pointerToSymbolTable: PointerToSymbolTable,
    val numberOfSymbols: NumberOfSymbols,
    val sizeOfOptionalHeader: SizeOfOptionalHeader,
    val characteristics: Characteristics,
): Header {
    companion object {
        const val SIZE = 20

        fun get(headerOffset: QWord, file: RandomAccessFile) =
            CoffHeader(
                targetMachine = TargetMachine(headerOffset, file),
                numberOfSections = NumberOfSections(headerOffset, file),
                timeDateStamp = TimeDateStamp(headerOffset, file),
                pointerToSymbolTable = PointerToSymbolTable(headerOffset, file),
                numberOfSymbols = NumberOfSymbols(headerOffset, file),
                sizeOfOptionalHeader = SizeOfOptionalHeader(headerOffset, file),
                characteristics = Characteristics(headerOffset, file),
            )
    }

    override val headerName = "COFF Header"

    override fun toString(): String {
        return """
            |$headerName:
            |    ${targetMachine.realName}: ${targetMachine.hex}
            |    ${numberOfSections.realName}: ${numberOfSections.hex}
            |    ${timeDateStamp.realName}: ${timeDateStamp.hex}
            |    ${pointerToSymbolTable.realName}: ${pointerToSymbolTable.hex}
            |    ${numberOfSymbols.realName}: ${numberOfSymbols.hex}
            |    ${sizeOfOptionalHeader.realName}: ${sizeOfOptionalHeader.hex}
            |    ${characteristics.realName}: ${characteristics.hex}
        """.trimMargin()
    }
}