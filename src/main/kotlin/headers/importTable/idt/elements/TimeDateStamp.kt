package std.student.headers.importTable.idt.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.importTable.idt.ImportDirectoryTableElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class TimeDateStamp : ImportDirectoryTableElement {
    override val data: DWord
    override val realOffset: QWord

    override val relativeOffset: QWord = 4

    override val hex: String get() = data.hex
    override val realName = "Time/Date Stamp"
    override val description = """
        The stamp that is set to zero until the image is bound. 
        After the image is bound, this field is set to the time/data stamp of the DLL. 
    """.trimIndent()

    constructor(fileOffset: QWord, file: RandomAccessFile) {
        realOffset = fileOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.dword
    }

    constructor(original: TimeDateStamp, newData: DWord) {
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