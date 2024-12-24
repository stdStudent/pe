package std.student.headers

import arrow.core.Either
import std.student.conventions.QWord
import java.io.RandomAccessFile

interface Element<T> {
    val data: T
    val realOffset: QWord

    val hex: String
    val realName: String
    val description: String

    fun getDataType(): String {
        val type = this::class.supertypes[0].arguments[0].type ?: return "Unknown"
        val classifierType = type.classifier.toString().split(".").last()
        val arguments = type.arguments.map { it.type?.classifier.toString().split(".").last() }

        return if (classifierType == "Either") {
            val either = (data as Either<*, *>)
            if (either.isLeft()) "Left<${arguments[0]}>" else "Right<${arguments[1]}>"
        } else {
            if (arguments.isNotEmpty())
                "$classifierType<${arguments.joinToString(", ")}>"
            else
                classifierType
        }
    }
}

interface EmbeddableElement<T>: Element<T> {
    fun embed(file: RandomAccessFile)

    fun <T> getCopy(newData: T): EmbeddableElement<*> =
        this::class.constructors.elementAt(1).call(this, newData)
}
