package std.student.headers

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

interface Header {
    val headerName: String

    companion object {
        /**
         * Get all properties of the data class that are passed to the primary constructor.
         * ```kotlin
         * data class Sample(val a: Int, val b: String)
         * val sample = Sample(1, "2")
         * val properties: List<Any> = sample.getProperties()
         * ```
         * @param D the data class type
         * @param T the type of the properties
         */
        inline fun <reified T, reified D : Any> D.getProperties(): List<T> {
            val kClass = this::class as KClass<D>

            val primaryConstructor = kClass.primaryConstructor ?: return emptyList()
            val primaryConstructorParameters = primaryConstructor.parameters

            return primaryConstructorParameters
                .mapNotNull { param ->
                    kClass.memberProperties.find { it.name == param.name }?.get(this) as? T
                }
        }
    }
}