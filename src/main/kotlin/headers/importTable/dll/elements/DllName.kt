package std.student.headers.importTable.dll.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.hex
import std.student.headers.importTable.dll.DLLElement
import std.student.headers.pe.headers.section.SectionHeader
import std.student.utils.BufferUtils
import std.student.utils.RvaUtils.toFileOffset
import java.io.RandomAccessFile

class DllName : DLLElement {
    override val data: ByteArray
    override val realOffset: QWord

    override val size: DWord

    override val hex: String get() = data.joinToString { it.hex }
    override val realName: String get() = "DLL name"
    override val description: String get() = """
        Name of the DLL.
    """.trimIndent()

    constructor(nameRVA: DWord, sectionsHeaders: List<SectionHeader>, file: RandomAccessFile) {
        realOffset = nameRVA.toFileOffset(sectionsHeaders)

        val dllName = buildString {
            file.seek(realOffset)
            while (true) {
                val byte = file.readByte()
                if (byte == 0.toByte())
                    break

                append(byte.toInt().toChar())
            }
        }

        size = dllName.length
        data = dllName.toByteArray()
    }

    constructor(original: DllName, newData: ByteArray) {
        if (newData.size > original.size)
            throw IllegalArgumentException("$realName must be no more than ${original.size} bytes long.")

        val remainingZeroes = if (newData.size < original.size) {
            val diff = original.size - newData.size
            ByteArray(diff) { 0 }
        } else
            byteArrayOf()

        data = newData + remainingZeroes
        size = original.size
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            put(data)
            flip()
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }

    override fun toString() = String(data)
}