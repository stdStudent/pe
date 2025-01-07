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

private fun String.isSingleQuoted(): Boolean = this.startsWith("'") && this.endsWith("'")
private fun String.isDoubleQuoted(): Boolean = this.startsWith("\"") && this.endsWith("\"")

/**
 * Example: 'MZ' to "0x5A4D"
 */
private fun String.getNumericRepresentation(): String =
    this
        .substring(1, this.length - 1)
        .map { it.code.toString(16).padStart(2, '0') }
        .reversed()
        .joinToString("")
        .let { "0x$it" }

/**
 * Example: "abc" to "97 98 99"
 */
private fun String.getArrayRepresentation(): String =
    this
        .substring(1, this.length - 1)
        .map { it.code.toString() }
        .joinToString(" ")

private fun String.convert(): Long = when {
    this.startsWith("0x") -> {
        val value = this.substring(2)
        value.toLong(16)
    }

    this.endsWith("h") -> {
        val value = this.substring(0, this.length - 1)
        value.toLong(16)
    }

    else -> this.toLong(10)
}

fun String.getInstance(arg: String): Any {
    val value = when {
        arg.isSingleQuoted() -> arg.getNumericRepresentation()
        arg.isDoubleQuoted() -> arg.getArrayRepresentation()
        else -> arg
    }

    return when (this) {
        // Primitive types
        "Byte" -> value.convert().toByte()
        "Short" -> value.convert().toShort()
        "Int" -> value.convert().toInt()
        "Long" -> value.convert().toLong()

        // Collection types
        "ByteArray" -> value.split(" ").map { it.convert().toByte() }.toByteArray()
        "List<Byte>" -> value.split(" ").map { it.convert().toByte() }
        "List<Short>" -> value.split(" ").map { it.convert().toShort() }
        "List<Int>" -> value.split(" ").map { it.convert().toInt() }
        "List<Long>" -> value.split(" ").map { it.convert().toLong() }

        // Arrow types
        "Left<Byte>" -> value.convert().toByte().left()
        "Left<Short>" -> value.convert().toShort().left()
        "Left<Int>" -> value.convert().toInt().left()
        "Left<Long>" -> value.convert().toLong().left()

        "Right<Byte>" -> value.convert().toByte().right()
        "Right<Short>" -> value.convert().toShort().right()
        "Right<Int>" -> value.convert().toInt().right()
        "Right<Long>" -> value.convert().toLong().right()

        else -> throw InternalException("Unknown type.")
    }
}
