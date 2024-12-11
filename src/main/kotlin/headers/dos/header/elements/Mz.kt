package std.student.headers.dos.header.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.Word
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class Mz: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 0
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "MZ [e_magic]"
    override val description: String get() = """
        Specifies the magic number, specifically the Windows OS signature value, 
        which can either take on MZ for DOS (which is, for all intensive purposes, MOST Windows executables), 
        NE for OS2, LE for OS2 LE, or PE00 for NT.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: Mz, newData: Word) {
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