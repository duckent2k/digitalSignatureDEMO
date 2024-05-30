package com.example.digitalsignature

import android.content.Context
import android.content.SharedPreferences
import java.lang.UnsupportedOperationException

// save value to shared preferences
inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

fun Context.saveValueToPref(key: String, value: Any) {
    val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
    pref.edit {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            else -> throw UnsupportedOperationException("This type can be saved into Preferences")
        }
    }
}

// get value from shared preference
inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when (T::class) {
        String::class -> getString(key, defaultValue as String) as T
        Int::class -> getInt(key, defaultValue as Int) as T
        Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
        Float::class -> getFloat(key, defaultValue as Float) as T
        Long::class -> getLong(key, defaultValue as Long) as T
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}

inline fun <reified T> Context.getValueFromPref(key: String, defaultValue: T): T {
    val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
    return pref.get(key, defaultValue) as T
}
