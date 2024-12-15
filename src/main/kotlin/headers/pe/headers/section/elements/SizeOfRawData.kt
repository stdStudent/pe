package std.student.headers.pe.headers.section.elements

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.dword
import std.student.conventions.hex
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class SizeOfRawData: PeElement<DWord> {
    override val data: DWord
    override val realOffset: QWord
    override val peType = PeType.Pe32(
        relativeOffset = 16,
        size = 4,
    )

    override val hex: String get() = data.hex
    override val realName = "Size of Raw Data"
    override val description = """
        The size of the section (for object files) or the size of the initialized data on disk (for image files). 
        For executable images, this must be a multiple of FileAlignment from the optional header. 
        If this is less than VirtualSize, the remainder of the section is zero-filled. 
        Because the SizeOfRawData field is rounded but the VirtualSize field is not, 
        it is possible for SizeOfRawData to be greater than VirtualSize as well. 
        When a section contains only uninitialized data, this field should be zero.
    """.trimIndent()

    constructor(headerOffset: QWord, file: RandomAccessFile) {
        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)
        data = buffer.dword
    }

    constructor(original: SizeOfRawData, newData: DWord) {
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