package std.student.headers.pe.headers.optional.elements.standard

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class AddressOfEntryPoint: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 16,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "Address of Entry Point"
    override val description = """
        The address of the entry point relative to the image base when the executable file is loaded into memory. 
        For program images, this is the starting address. 
        For device drivers, this is the address of the initialization function. 
        An entry point is optional for DLLs. When no entry point is present, this field must be zero. 
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: AddressOfEntryPoint, newData: DWord) {
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