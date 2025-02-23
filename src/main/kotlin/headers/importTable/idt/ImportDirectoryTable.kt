package std.student.headers.importTable.idt

import std.student.conventions.QWord
import std.student.headers.Header
import std.student.headers.importTable.idt.elements.ForwarderChain
import std.student.headers.importTable.idt.elements.ImportAddressTableRVA
import std.student.headers.importTable.idt.elements.ImportLookupTableRVA
import std.student.headers.importTable.idt.elements.NameRVA
import std.student.headers.importTable.idt.elements.TimeDateStamp
import java.io.RandomAccessFile

data class ImportDirectoryTable(
    val importLookupTableRva: ImportLookupTableRVA,
    val timeDateStamp: TimeDateStamp,
    val forwarderChain: ForwarderChain,
    val nameRva: NameRVA,
    val importAddressTableRva: ImportAddressTableRVA,
): Header {
    companion object {
        const val SIZE = 20

        fun get(fileOffset: QWord, file: RandomAccessFile) =
            ImportDirectoryTable(
                importLookupTableRva = ImportLookupTableRVA(fileOffset, file),
                timeDateStamp = TimeDateStamp(fileOffset, file),
                forwarderChain = ForwarderChain(fileOffset, file),
                nameRva = NameRVA(fileOffset, file),
                importAddressTableRva = ImportAddressTableRVA(fileOffset, file),
            )
    }

    override val headerName: String = "Import Directory Table"

    override fun toString(): String {
        return """
            |Import Directory Table:
            |    ${importLookupTableRva.realName}: ${importLookupTableRva.hex}
            |    ${timeDateStamp.realName}: ${timeDateStamp.hex}
            |    ${forwarderChain.realName}: ${forwarderChain.hex}
            |    ${nameRva.realName}: ${nameRva.hex}
            |    ${importAddressTableRva.realName}: ${importAddressTableRva.hex}
        """.trimMargin()
    }
}