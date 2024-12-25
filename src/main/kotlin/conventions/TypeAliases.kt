package std.student.conventions

import arrow.core.left
import arrow.core.right
import com.sun.jdi.InternalException
import java.nio.ByteBuffer

typealias Word = Short
typealias DWord = Int
typealias QWord = Long

fun DWord.toQWord(): QWord = this.toLong()
fun QWord.toDWord(): DWord = this.toInt()

fun Number.toHex(padding: UInt = 0u) = "0x" + "%0${ if (padding > 0u) padding else 1u }x".format(this).uppercase()
fun String.toDecimal(): String = this.substring(2).toLong(16).toString()

inline val Byte.hex: String get() = this.toHex(2u)
inline val Word.hex: String get() = this.toHex(4u)
inline val DWord.hex: String get() = this.toHex(8u)
inline val QWord.hex: String get() = this.toHex(16u)

inline var ByteBuffer.byte: Byte
    get() = this.get()
    set(value) {
        this.put(value)
    }

inline var ByteBuffer.word: Word
    get() = this.short
    set(value) {
        this.putShort(value)
    }

inline var ByteBuffer.dword: DWord
    get() = this.int
    set(value) {
        this.putInt(value)
    }

inline var ByteBuffer.qword: QWord
    get() = this.long
    set(value) {
        this.putLong(value)
    }

// ----------------------
// Reflection type traits

private fun String.convert(): String = when {
    this.startsWith("0x") -> {
        val value = this.substring(2)
        value.toLong(16).toString()
    }

    this.endsWith("h") -> {
        val value = this.substring(0, this.length - 1)
        value.toLong(16).toString()
    }

    else -> this
}

fun String.getInstance(arg: String): Any {
    return when (this) {
        // Primitive types
        "Byte" -> arg.convert().toByte()
        "Short" -> arg.convert().toShort()
        "Int" -> arg.convert().toInt()
        "Long" -> arg.convert().toLong()

        // Collection types
        "ByteArray" -> arg.split(" ").map { it.convert().toByte() }.toByteArray()
        "List<Byte>" -> arg.split(" ").map { it.convert().toByte() }
        "List<Short>" -> arg.split(" ").map { it.convert().toShort() }
        "List<Int>" -> arg.split(" ").map { it.convert().toInt() }
        "List<Long>" -> arg.split(" ").map { it.convert().toLong() }

        // Arrow types
        "Left<Byte>" -> arg.convert().toByte().left()
        "Left<Short>" -> arg.convert().toShort().left()
        "Left<Int>" -> arg.convert().toInt().left()
        "Left<Long>" -> arg.convert().toLong().left()

        "Right<Byte>" -> arg.convert().toByte().right()
        "Right<Short>" -> arg.convert().toShort().right()
        "Right<Int>" -> arg.convert().toInt().right()
        "Right<Long>" -> arg.convert().toLong().right()

        else -> throw InternalException("Unknown type.")
    }
}
