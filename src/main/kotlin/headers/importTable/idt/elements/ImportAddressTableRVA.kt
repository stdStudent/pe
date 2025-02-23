package std.student.headers.importTable.idt.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.importTable.idt.ImportDirectoryTableElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class ImportAddressTableRVA : ImportDirectoryTableElement {
    override val data: DWord
    override val realOffset: QWord

    override val relativeOffset: QWord = 16

    override val hex: String get() = data.hex
    override val realName = "Import Address Table RVA [Thunk Table]"
    override val description = """
        The RVA of the import address table. 
        The contents of this table are identical to the contents of the import lookup table until the image is bound. 
    """.trimIndent()

    constructor(fileOffset: QWord, file: RandomAccessFile) {
        realOffset = fileOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.dword
    }

    constructor(original: ImportAddressTableRVA, newData: DWord) {
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