package std.student.headers.importTable.idt.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.importTable.idt.ImportDirectoryTableElement
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class ImportLookupTableRVA : ImportDirectoryTableElement {
    override val data: DWord
    override val realOffset: QWord

    override val relativeOffset: QWord = 0

    override val hex: String get() = data.hex
    override val realName = "Import Lookup Table RVA [Characteristics]"
    override val description = """
        The RVA of the import lookup table. 
        This table contains a name or ordinal for each import. 
        (The name "Characteristics" is used in Winnt.h, but no longer describes this field.) 
    """.trimIndent()

    constructor(fileOffset: QWord, file: RandomAccessFile) {
        realOffset = fileOffset + relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, size)
        data = buffer.dword
    }

    constructor(original: ImportLookupTableRVA, newData: DWord) {
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