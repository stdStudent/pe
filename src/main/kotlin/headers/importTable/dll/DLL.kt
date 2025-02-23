package std.student.headers.importTable.dll

import std.student.conventions.DWord
import std.student.headers.Header
import std.student.headers.importTable.dll.elements.DllName
import std.student.headers.pe.headers.section.SectionHeader
import java.io.RandomAccessFile

data class DLL(
    val name: DllName,
): Header {
    companion object {
        fun get(nameRVA: DWord, sectionsHeaders: List<SectionHeader>, file: RandomAccessFile) =
            DLL(name = DllName(nameRVA, sectionsHeaders, file))
    }

    override val headerName: String = "DLL"

    override fun toString(): String {
        return """
            |DLL:
            |    ${name.realName}: $name
        """.trimMargin()
    }
}