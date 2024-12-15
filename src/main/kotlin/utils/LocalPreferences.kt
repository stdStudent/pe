package std.student.utils

import java.util.prefs.Preferences

object LocalPreferences {
    private const val NODE_NAME = "std/student/pe/settings"
    private val preferences = Preferences.userRoot().node(NODE_NAME)

    private inline fun <reified T> set(key: String, value: T) =
        when (value) {
            is String -> preferences.put(key, value)
            is Int -> preferences.putInt(key, value)
            is Long -> preferences.putLong(key, value)
            is Float -> preferences.putFloat(key, value)
            is Double -> preferences.putDouble(key, value)
            is Boolean -> preferences.putBoolean(key, value)
            is ByteArray -> preferences.putByteArray(key, value)
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.java}")
        }

    private inline fun <reified T> get(key: String): T =
        when (T::class) {
            String::class -> preferences.get(key, "") as T
            Int::class -> preferences.getInt(key, 0) as T
            Long::class -> preferences.getLong(key, 0) as T
            Float::class -> preferences.getFloat(key, 0f) as T
            Double::class -> preferences.getDouble(key, 0.0) as T
            Boolean::class -> preferences.getBoolean(key, false) as T
            ByteArray::class -> preferences.getByteArray(key, byteArrayOf()) as T
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.java}")
        }

    object Settings {
        private const val LAST_OPENED_DIR_KEY = "last_opened_dir"
        var lastOpenedDir: String
            get() = get(LAST_OPENED_DIR_KEY)
            set(value) = set(LAST_OPENED_DIR_KEY, value)
    }
}