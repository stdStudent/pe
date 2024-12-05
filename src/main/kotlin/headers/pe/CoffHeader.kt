package std.student.headers.pe

import std.student.conventions.*
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
            |   target machine: ${targetMachine.hex}
            |   number of sections: ${numberOfSections.hex}
            |   time date stamp: ${timeDateStamp.hex}
            |   pointer to symbol table: ${pointerToSymbolTable.hex}
            |   number of symbols: ${numberOfSymbols.hex}
            |   size of optional header: ${sizeOfOptionalHeader.hex}
            |   characteristics: ${characteristics.hex}
        """.trimMargin()
    }
}
