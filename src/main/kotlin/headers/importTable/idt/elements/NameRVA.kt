package std.student.headers.importTable.idt.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.importTable.idt.ImportDirectoryTableElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class NameRVA : ImportDirectoryTableElement {
    override val data: DWord
    override val realOffset: QWord

    override val relativeOffset: QWord = 12

    override val hex: String get() = data.hex
    override val realName = "Name RVA"
    override val description = """
        The address of an ASCII string that contains the name of the DLL.
        This address is relative to the image base.
    """.trimIndent()

    constructor(fileOffset: QWord, file: RandomAccessFile) {
        realOffset = fileOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.dword
    }

    constructor(original: NameRVA, newData: DWord) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(size).apply {
            this.dword = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}