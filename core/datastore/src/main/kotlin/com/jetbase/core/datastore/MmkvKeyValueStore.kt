package com.jetbase.core.datastore

import com.tencent.mmkv.MMKV

/** Real implementation with MMKV (mmap: very fast synchronous reads, atomic writes). */
internal class MmkvKeyValueStore(private val mmkv: MMKV) : KeyValueStore {
    override fun getString(key: String, default: String): String = mmkv.decodeString(key) ?: default

    override fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    override fun getInt(key: String, default: Int): Int = mmkv.decodeInt(key, default)

    override fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean = mmkv.decodeBool(key, default)

    override fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun contains(key: String): Boolean = mmkv.containsKey(key)

    override fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }
}
