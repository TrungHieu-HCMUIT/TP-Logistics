package com.trunnghieu.tplogisticsapplication.data.api.helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.nio.charset.StandardCharsets

object JsonHelper {

    /**
     * Convert data object to json
     */
    fun <T> toString(dataObject: T): String {
        return Gson().toJson(dataObject, object : TypeToken<T>() {}.type)
    }

    /**
     * Convert json to data object
     */
    inline fun <reified T> toObject(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    /**
     * Read json from assets
     */
    fun readFromAssets(context: Context, fileName: String): String? {
        var json: String?
        try {
            context.assets.open(fileName)
                .run {
                    val size = available()
                    val buffer = ByteArray(size)
                    read(buffer)
                    close()
                    json = String(buffer, StandardCharsets.UTF_8)
                }
        } catch (exception: IOException) {
            exception.printStackTrace()
            return null
        }
        return json
    }
}