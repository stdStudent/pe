package std.student.conventions

import java.nio.ByteBuffer

typealias Word = Short
typealias DWord = Int

inline val Byte.hex: String get() = "%02x".format(this)
inline val Word.hex: String get() = "%04x".format(this)
inline val DWord.hex: String get() = "%08x".format(this)

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
