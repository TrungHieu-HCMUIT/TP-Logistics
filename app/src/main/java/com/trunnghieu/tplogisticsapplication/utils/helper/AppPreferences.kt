package com.trunnghieu.tplogisticsapplication.utils.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson

class AppPreferences private constructor() {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    @SuppressLint("CommitPrefEdits")
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
    }

    /**
     * Store value to SharedPreferences
     */
    fun storeValue(key: String?, value: Any?): Boolean {
        if (value is String) {
            editor?.putString(key, value as String?)
        }
        if (value is Int) {
            editor?.putInt(key, (value as Int?)!!)
        }
        if (value is Long) {
            editor?.putLong(key, (value as Long?)!!)
        }
        if (value is Float) {
            editor?.putFloat(key, (value as Float?)!!)
        }
        if (value is Double) {
            editor?.putLong(key, java.lang.Double.doubleToRawLongBits((value as Double?)!!))
        }
        if (value is Boolean) {
            editor?.putBoolean(key, (value as Boolean?)!!)
        }
        if (value is Parcelable) {
            val stringObject = Gson().toJson(value)
            editor?.putString(key, stringObject)
        }
        if (value == null) {
            editor?.remove(key)
        }
        return editor?.commit() ?: false
    }

    /**
     * Get String
     */
    @Synchronized
    fun getString(key: String?): String {
        return getString(key, null)
    }

    /**
     * Get String with default value
     */
    @Synchronized
    fun getString(key: String?, defValue: String?): String {
        return sharedPreferences?.getString(key, defValue) ?: ""
    }

    /**
     * Get Int
     *
     * @param key
     * @return
     */
    @Synchronized
    fun getInt(key: String?): Int {
        return getInt(key, 0)
    }

    /**
     * Get Int with default value
     */
    @Synchronized
    fun getInt(key: String?, defValue: Int): Int {
        return sharedPreferences?.getInt(key, defValue) ?: 0
    }

    /**
     * Get Long
     *
     * @param key
     * @return
     */
    @Synchronized
    fun getLong(key: String?): Long {
        return getLong(key, 0L)
    }

    /**
     * Get Long with default value
     *
     * @param key
     * @param defValue
     * @return
     */
    @Synchronized
    fun getLong(key: String?, defValue: Long): Long {
        return sharedPreferences?.getLong(key, defValue) ?: 0L
    }

    /**
     * Get Float
     *
     * @param key
     * @return
     */
    @Synchronized
    fun getFloat(key: String?): Float {
        return getFloat(key, 0f)
    }

    /**
     * Get Float with default value
     *
     * @param key
     * @param defValue
     * @return
     */
    @Synchronized
    fun getFloat(key: String?, defValue: Float): Float {
        return sharedPreferences?.getFloat(key, defValue) ?: 0f
    }

    /**
     * Get Double
     *
     * @param key
     * @return
     */
    @Synchronized
    fun getDouble(key: String?): Double {
        return getDouble(key, 0.0)
    }

    /**
     * Get Double with default value
     *
     * @param key
     * @param defValue
     * @return
     */
    @Synchronized
    fun getDouble(key: String?, defValue: Double): Double {
        return java.lang.Double.longBitsToDouble(
            getLong(
                key,
                java.lang.Double.doubleToLongBits(defValue)
            )
        )
    }

    /**
     * Get Boolean
     */
    fun getBoolean(key: String?): Boolean {
        return getBoolean(key, false)
    }

    /**
     * Get Boolean with default value
     */
    @Synchronized
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return sharedPreferences?.getBoolean(key, defValue) ?: false
    }

    /**
     * Get Parcelable
     */
    @Synchronized
    fun <T : Parcelable> getParcelableObject(key: String?, type: Class<T>?): T? {
        val string = sharedPreferences?.getString(key, null) ?: return null
        return Gson().fromJson(string, type)
    }

    /**
     * Clear all data except provided key
     */
    fun clearAllExcept(vararg key: String?) {
        sharedPreferences?.run {
            all?.keys?.forEach { existKey ->
                if (!key.contains(existKey)) {
                    edit().remove(existKey).apply()
                    return@forEach
                }
            }
        }
    }

    /**
     * Clear all preferences
     */
    fun clearAll() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    companion object {

        @Volatile
        private var instance: AppPreferences? = null

        fun get(): AppPreferences =
            instance ?: synchronized(this) {
                val newInstance = instance ?: AppPreferences()
                    .also { instance = it }
                newInstance
            }
    }
}