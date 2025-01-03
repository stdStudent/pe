package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class InitialRelativeIP: DosElement<Word> {
    override val data: Word
    override val realOffset: QWord

    override val relativeOffset: QWord = 20
    override val size: DWord = 2

    override val hex: String get() = data.hex
    override val realName: String get() = "Initial Relative IP [e_ip]"
    override val description: String get() = """
        Specifies the initial IP value, which is the absolute value 
        that should be loaded into the IP register in order to transfer control to the program. 
        Since the actual code segment is determined by the loader, 
        and this is merely a value within that segment, it does not need to be relocated.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.word
    }

    constructor(original: InitialRelativeIP, newData: Word) {
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