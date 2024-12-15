package std.student.headers.pe.headers.section.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class VirtualAddress: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 12,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "Virtual Address"
    override val description = """
        For executable images, the address of the first byte of the section relative to 
        the image base when the section is loaded into memory. 
        For object files, this field is the address of the first byte before relocation is applied; 
        for simplicity, compilers should set this to zero. 
        Otherwise, it is an arbitrary value that is subtracted from offsets during relocation. 
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: VirtualAddress, newData: DWord) {
        data = newData
        realOffset = original.realOffset
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size).apply {
            this.dword = data
        }

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}