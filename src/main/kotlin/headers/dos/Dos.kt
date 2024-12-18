package std.student.headers.dos

import std.student.headers.dos.header.DosHeader
import std.student.headers.dos.stub.DosStub
import java.io.RandomAccessFile

class Dos {
    val header: DosHeader
    val stub: DosStub

    constructor(file: RandomAccessFile) {
        header = DosHeader.get(file)
        stub = DosStub.get(file)
    }

    override fun toString(): String {
        return """
            |${header}
            |
            |${stub}
        """.trimMargin()
    }
}