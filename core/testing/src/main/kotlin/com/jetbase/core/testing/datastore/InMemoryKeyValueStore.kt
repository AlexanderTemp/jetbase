package com.jetbase.core.testing.datastore

import com.jetbase.core.datastore.KeyValueStore

/** KeyValueStore backed by a HashMap: replaces MMKV in JVM tests (MMKV needs native code). */
class InMemoryKeyValueStore : KeyValueStore {
    private val values = mutableMapOf<String, Any>()

    override fun getString(key: String, default: String): String = values[key] as? String ?: default

    override fun putString(key: String, value: String) {
        values[key] = value
    }

    override fun getInt(key: String, default: Int): Int = values[key] as? Int ?: default

    override fun putInt(key: String, value: Int) {
        values[key] = value
    }

    override fun getBoolean(key: String, default: Boolean): Boolean =
        values[key] as? Boolean ?: default

    override fun putBoolean(key: String, value: Boolean) {
        values[key] = value
    }

    override fun contains(key: String): Boolean = key in values

    override fun remove(key: String) {
        values.remove(key)
    }
}
