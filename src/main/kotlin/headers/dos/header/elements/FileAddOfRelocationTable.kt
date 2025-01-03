package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class FileAddOfRelocationTable: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 24
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "File Address Of Relocation Table [e_lfarlc]"
    override val description: String get() = """
        Specifies the file address of the relocation table, or more specifically, 
        the offset from the start of the file to the relocation pointer table. 
        This value must be used to locate the relocation pointer table 
        (rather than assuming a fixed location) because variable-length information 
        pertaining to program overlays can occur before this table, causing its position to vary. 
        A value of 0x40 in this field generally indicates a different kind of executable file, not a DOS 'MZ' type.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: FileAddOfRelocationTable, newData: Word) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            this.word = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}