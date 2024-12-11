package std.student.utils

import std.student.conventions.DWord
import std.student.conventions.QWord
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ByteOrder.LITTLE_ENDIAN

object BufferUtils {
    fun getBuffer(file: RandomAccessFile, startOffset: QWord, size: DWord, byteOrder: ByteOrder = LITTLE_ENDIAN) =
        ByteBuffer.wrap(
            ByteArray(size).apply {
                file.seek(startOffset)
                file.read(this)
            }
        ).order(byteOrder)

    fun getEmptyBuffer(size: DWord, byteOrder: ByteOrder = LITTLE_ENDIAN) =
        ByteBuffer.allocate(size).order(byteOrder)
}
