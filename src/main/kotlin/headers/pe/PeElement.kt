package std.student.headers.pe

import std.student.headers.EmbeddableElement
import std.student.headers.pe.type.PeType

interface PeElement<T>: EmbeddableElement<T> {
    val peType: PeType

    override fun <T> getCopy(newData: T): PeElement<*> =
        super.getCopy(newData) as PeElement<*>
}
