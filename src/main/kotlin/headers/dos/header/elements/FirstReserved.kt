package std.student.headers.dos.header.elements

import std.student.conventions.Word
import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.conventions.word
import std.student.headers.dos.DosElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class FirstReserved: DosElement<List<Word>> {
    override val data: List<Word>
    override val realOffset: QWord

    override val relativeOffset: QWord = 28
    override val size: DWord = 2 * 4

    override val hex: String get() = data.joinToString { it.hex }
    override val realName: String get() = "First Reserved [reserved1]"
    override val description: String get() = """
        Specifies reserved words for the program (known in winnt.h as e_res[4]), usually set to zero by the linker. 
        In this case, just use a single reserved1 set to zero; if not zero create four reserved1 with the correct value.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = (0 until 4).map { buffer.word }
    }

    constructor(original: FirstReserved, newData: List<Word>) {
        if (newData.size > 4)
            throw IllegalArgumentException("$realName must have no more than 4 elements.")

        val remainingZeroes = if (newData.size < 4) {
            val diff = 4 - newData.size
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