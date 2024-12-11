package std.student.conventions

import java.nio.ByteBuffer

typealias Word = Short
typealias DWord = Int
typealias QWord = Long

fun DWord.toQWord(): QWord = this.toLong()

inline val Byte.hex: String get() = "%02x".format(this)
inline val Word.hex: String get() = "%04x".format(this)
inline val DWord.hex: String get() = "%08x".format(this)
inline val QWord.hex: String get() = "%016x".format(this)

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
