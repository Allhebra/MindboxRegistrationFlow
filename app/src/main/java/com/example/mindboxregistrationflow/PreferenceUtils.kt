package com.example.mindboxregistrationflow

import android.content.Context
import android.content.SharedPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject

object PreferenceUtils : KoinComponent {
    private val appContext: Context by inject()
    private const val PREFS_NAME = "PREFS_NAME"

    private val prefs: SharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun set(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun set(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun set(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun set(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String = prefs.getString(key, defaultValue)
        ?: defaultValue

    fun getInt(name: String, defaultValue: Int = 0): Int = prefs.getInt(name, defaultValue)

    fun getLong(name: String, defaultValue: Long = 0): Long = prefs.getLong(name, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = prefs.getBoolean(key, defaultValue)
}