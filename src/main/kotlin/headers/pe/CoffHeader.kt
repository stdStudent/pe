package std.student.headers.pe

import std.student.conventions.DWord
import std.student.conventions.Word
import std.student.conventions.dword
import std.student.conventions.word
import java.nio.ByteBuffer

data class CoffHeader(
    val targetMachine: Word,
    val numberOfSections: Word,
    val timeDateStamp: DWord,
    val pointerToSymbolTable: DWord,
    val numberOfSymbols: DWord,
    val sizeOfOptionalHeader: Word,
    val characteristics: Word,
) {
    companion object {
        const val SIZE = 20

        fun get(buffer: ByteBuffer) =
            CoffHeader(
                targetMachine = buffer.word,
                numberOfSections = buffer.word,
                timeDateStamp = buffer.dword,
                pointerToSymbolTable = buffer.dword,
                numberOfSymbols = buffer.dword,
                sizeOfOptionalHeader = buffer.word,
                characteristics = buffer.word
            )
    }

    override fun toString(): String {
        return """
            |COFF HEADER:
            |   target machine: $targetMachine
            |   number of sections: $numberOfSections
            |   time date stamp: $timeDateStamp
            |   pointer to symbol table: $pointerToSymbolTable
            |   number of symbols: $numberOfSymbols
            |   size of optional header: $sizeOfOptionalHeader
            |   characteristics: $characteristics
        """.trimMargin()
    }
}
