package com.trunnghieu.tplogisticsapplication.utils


import com.orhanobut.logger.Logger
import com.trunnghieu.tplogisticsapplication.utils.constants.Const

object CustomLogger {

    private const val showLog = Const.DEBUG_MODE

    fun d(message: String) {
        if (showLog) {
            Logger.d("✅ $message")
        }
    }

    fun e(message: String) {
        if (showLog) {
            Logger.e("⛔️ $message")
        }
    }

    fun w(message: String) {
        if (showLog) {
            Logger.wtf(message)
        }
    }

    fun json(message: String) {
        if (showLog) {
            Logger.json(message)
        }
    }

    fun i(message: String) {
        if (showLog) {
            Logger.i("ℹ️ $message")
        }
    }

    fun v(message: String) {
        if (showLog) {
            Logger.v(message)
        }
    }

    fun xml(message: String) {
        if (showLog) {
            Logger.xml(message)
        }
    }


}