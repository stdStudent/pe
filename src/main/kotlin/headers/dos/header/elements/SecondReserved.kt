package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class SecondReserved: DosElement<List<Word>> {
    override val data: List<Word>
    override val realOffset: QWord

    override val relativeOffset: QWord = 40
    override val size: DWord = 2 * 10

    override val hex: String get() = data.joinToString { it.hex }
    override val realName: String get() = "Second Reserved [reserved2]"
    override val description: String get() = """
        Specifies reserved words for the program (known in winnt.h as e_res[10]), usually set to zero by the linker. 
        In this case, just use a single reserved1 set to zero; if not zero create ten reserved1 with the correct value.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = (0 until 10).map { buffer.word }
    }

    constructor(original: SecondReserved, newData: List<Word>) {
        if (newData.size > 10)
            throw IllegalArgumentException("$realName must have no more than 10 elements.")

        val remainingZeroes = if (newData.size < 10) {
            val diff = 10 - newData.size
            List<Word>(diff) { 0 }
        } else
            emptyList()

        data = newData + remainingZeroes
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            data.forEach { word ->
                this.word = word
            }
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}