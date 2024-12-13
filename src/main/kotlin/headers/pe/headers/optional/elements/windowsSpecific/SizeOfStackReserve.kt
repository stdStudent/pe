package std.student.headers.pe.headers.optional.elements.windowsSpecific

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import std.student.conventions.*
import std.student.headers.pe.PeElement
import std.student.headers.pe.type.PeType
import std.student.headers.pe.type.PeType.Companion.PE32_MAGIC
import std.student.headers.pe.type.PeType.Companion.PE32_PLUS_MAGIC
import std.student.headers.pe.type.PeType.Companion.isCorrectType
import std.student.utils.BufferUtils
import java.io.RandomAccessFile

class SizeOfStackReserve: PeElement<Either<DWord, QWord>> {
    override val data: Either<DWord, QWord>
    override val realOffset: QWord
    override val peType: PeType

    override val hex: String get() = data.fold({ it.hex }, { it.hex })
    override val realName = "Size of Stack Reserve"
    override val description = """
	    The size of the stack to reserve. 
        Only SizeOfStackCommit is committed; the rest is made available one page at a time 
        until the reserve size is reached. 
    """.trimIndent()

    constructor(headerOffset: QWord, magic: Word, file: RandomAccessFile) {
        peType = when (magic) {
            PE32_MAGIC -> PeType.Pe32(72, 4)
            PE32_PLUS_MAGIC -> PeType.Pe32Plus(72, 8)
            else -> throw IllegalArgumentException("Invalid magic number: ${magic.hex}")
        }

        realOffset = headerOffset + peType.relativeOffset
        val buffer = BufferUtils.getBuffer(file, realOffset, peType.size)

        data = if (peType is PeType.Pe32)
            buffer.dword.left()
        else
            buffer.qword.right()
    }

    constructor(original: SizeOfStackReserve, newData: Either<DWord, QWord>) {
        if (newData.isCorrectType(original.peType).not())
            throw IllegalArgumentException("Invalid data type for PE type.")

        data = newData
        realOffset = original.realOffset
        peType = original.peType
    }

    override fun embed(file: RandomAccessFile) {
        val byteBuffer = BufferUtils.getEmptyBuffer(peType.size)

        if (peType is PeType.Pe32)
            byteBuffer.dword = data.fold({ it }, { 0 })
        else
            byteBuffer.qword = data.fold({ 0 }, { it })

        file.seek(realOffset)
        file.write(byteBuffer.array())
    }
}