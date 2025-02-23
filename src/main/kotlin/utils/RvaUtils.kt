package std.student.utils

import std.student.conventions.DWord
import std.student.conventions.QWord
import std.student.conventions.toQWord
import std.student.headers.pe.headers.section.SectionHeader

object RvaUtils {
    fun DWord.toFileOffset(sections: List<SectionHeader>): QWord {
        // Find the section that contains the RVA
        val section = sections.find { section ->
            section.virtualAddress.data <= this && this < section.virtualAddress.data + section.virtualSize.data
        }

        // Calculate the file offset
        val result = section!!.pointerToRawData.data + (this - section.virtualAddress.data)
        return result.toQWord()
    }
}