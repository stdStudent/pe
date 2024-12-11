package std.student.headers.pe.type

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.Word

interface PeTypeCharacteristic {
    val relativeOffset: QWord
    val size: DWord
}

sealed class PeType: PeTypeCharacteristic {
    data class Pe32(override val relativeOffset: QWord, override val size: DWord): PeType()
    data class Pe32Plus(override val relativeOffset: QWord, override val size: DWord): PeType()

    companion object {
        const val PE32_MAGIC: Word = 0x10b
        const val PE32_PLUS_MAGIC: Word = 0x20b
        const val ROM_IMAGE: Word = 0x107 // Might never be supported
    }
}
