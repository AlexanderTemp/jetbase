package com.jetbase.core.datastore

/**
 * Minimal contract of a key-value store.
 *
 * Seal calls MMKV directly from ~40 files (PreferenceUtil + extension functions on String). Here
 * MMKV is hidden behind this interface: if it is ever replaced by DataStore, only ONE class
 * ([MmkvKeyValueStore]) is rewritten, and tests use an in-memory version.
 */
interface KeyValueStore {
    fun getString(key: String, default: String): String

    fun putString(key: String, value: String)

    fun getInt(key: String, default: Int): Int

    fun putInt(key: String, value: Int)

    fun getBoolean(key: String, default: Boolean): Boolean

    fun putBoolean(key: String, value: Boolean)

    fun contains(key: String): Boolean

    fun remove(key: String)
}
