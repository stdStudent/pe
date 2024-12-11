package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class InitialRelativeSS: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 14
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "Initial Relative SS [e_ss]"
    override val description: String get() = """
        Specifies the initial SS value, which is the paragraph address 
        of the stack segment relative to the start of the load module. 
        At load time, this value is relocated by adding the address 
        of the start segment of the program to it, and the resulting value is placed 
        in the SS register before the program is started. 
        In DOS, the start segment of the program is the first segment boundary in memory after the PSP.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: InitialRelativeSS, newData: Word) {
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