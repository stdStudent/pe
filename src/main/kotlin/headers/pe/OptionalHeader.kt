package std.student.headers.pe

import std.student.conventions.DWord
import std.student.conventions.Word
import std.student.utils.BufferUtils
import java.io.RandomAccessFile
import java.nio.ByteOrder

class OptionalHeader {
    private val standardFieldsOffset: DWord
    private val standardFields: StandardFields

    constructor(startOffset: DWord, file: RandomAccessFile) {
        standardFieldsOffset = startOffset
        val standardFieldsBuffer = BufferUtils.getBuffer(file, standardFieldsOffset, StandardFields.PE32_SIZE, ByteOrder.LITTLE_ENDIAN)
        standardFields = StandardFields.get(standardFieldsBuffer)
    }

    constructor(
        startOffset: DWord,
        standardFields: StandardFields,
    ) {
        standardFieldsOffset = startOffset
        this.standardFields = standardFields
    }

    override fun toString(): String {
        return """
            |$standardFields
        """.trimMargin()
    }

    companion object {
        const val PE32_MAGIC: Word = 0x10b
        const val PE32_PLUS_MAGIC: Word = 0x20b
    }
}