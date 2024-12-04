package std.student.headers.dos

import std.student.conventions.DWord
import std.student.conventions.Word
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.conventions.word
import std.student.utils.BufferUtils
import java.io.RandomAccessFile
import java.nio.ByteOrder

data class DosHeader(
    val mZ: Word,
    val lastSize: Word,
    val pagesInFile: Word,
    val relocations: Word,
    val headerSizeInParagraph: Word,
    val minExtraParagraphs: Word,
    val maxExtraParagraphs: Word,
    val initialRelativeSS: Word,
    val initialRelativeSP: Word,
    val checksum: Word,
    val initialRelativeIP: Word,
    val initialRelativeCS: Word,
    val fileAddOfRelocationTable: Word,
    val overlayNumber: Word,
    val firstReserved: List<Word> = List(FIRST_RESERVED_SIZE) { 0 },
    val oemIdentifier: Word,
    val oemInformation: Word,
    val secondReserved: List<Word> = List(SECOND_RESERVED_SIZE) { 0 },
    val offsetToPeSignature: DWord,
) {
    companion object {
        private const val FIRST_RESERVED_SIZE = 4
        private const val SECOND_RESERVED_SIZE = 10

        const val START_OFFSET = 0x0L
        const val SIZE = 64

        fun get(file: RandomAccessFile): DosHeader {
            val buffer = BufferUtils.getBuffer(file, START_OFFSET, SIZE, ByteOrder.LITTLE_ENDIAN)

            return DosHeader(
                mZ = buffer.word,
                lastSize = buffer.word,
                pagesInFile = buffer.word,
                relocations = buffer.word,
                headerSizeInParagraph = buffer.word,
                minExtraParagraphs = buffer.word,
                maxExtraParagraphs = buffer.word,
                initialRelativeSS = buffer.word,
                initialRelativeSP = buffer.word,
                checksum = buffer.word,
                initialRelativeIP = buffer.word,
                initialRelativeCS = buffer.word,
                fileAddOfRelocationTable = buffer.word,
                overlayNumber = buffer.word,
                firstReserved = List(FIRST_RESERVED_SIZE) { buffer.word },
                oemIdentifier = buffer.word,
                oemInformation = buffer.word,
                secondReserved = List(SECOND_RESERVED_SIZE) { buffer.word },
                offsetToPeSignature = buffer.dword
            )
        }
    }

    override fun toString(): String {
        return """
            |DOS HEADER:
            |   mZ: ${mZ.hex}
            |   lastSize: ${lastSize.hex}
            |   pagesInFile: ${pagesInFile.hex}
            |   relocations: ${relocations.hex}
            |   headerSizeInParagraph: ${headerSizeInParagraph.hex}
            |   minExtraParagraphs: ${minExtraParagraphs.hex}
            |   maxExtraParagraphs: ${maxExtraParagraphs.hex}
            |   initialRelativeSS: ${initialRelativeSS.hex}
            |   initialRelativeSP: ${initialRelativeSP.hex}
            |   checksum: ${checksum.hex}
            |   initialRelativeIP: ${initialRelativeIP.hex}
            |   initialRelativeCS: ${initialRelativeCS.hex}
            |   fileAddOfRelocationTable: ${fileAddOfRelocationTable.hex}
            |   overlayNumber: ${overlayNumber.hex}
            |   firstReserved: ${firstReserved.joinToString { it.hex }}
            |   oemIdentifier: ${oemIdentifier.hex}
            |   oemInformation: ${oemInformation.hex}
            |   secondReserved: ${secondReserved.joinToString { it.hex }}
            |   offsetToPeSignature: ${offsetToPeSignature.hex}
        """.trimMargin()
    }
}
