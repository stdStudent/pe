package std.student.utils

import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

object BufferUtils {
    fun getBuffer(file: RandomAccessFile, startOffset: Long, size: Int, byteOrder: ByteOrder) =
        ByteBuffer.wrap(
            ByteArray(size).apply {
                file.seek(startOffset)
                file.read(this)
            }
        ).order(byteOrder)

    fun getBuffer(file: RandomAccessFile, startOffset: Int, size: Int, byteOrder: ByteOrder) =
        getBuffer(file, startOffset.toLong(), size, byteOrder)
}
