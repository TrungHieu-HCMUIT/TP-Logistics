package com.trunnghieu.tplogisticsapplication.utils.helper

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

object ServiceHelper {

    private fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun startService(context: Context, serviceClass: Class<*>) {
        if (!isMyServiceRunning(context, serviceClass)) {
            val service = Intent(context, serviceClass)
            context.startService(service)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startForegroundService(context: Context, serviceClass: Class<*>) {
        if (!isMyServiceRunning(context, serviceClass)) {
            val service = Intent(context, serviceClass)
            context.startForegroundService(service)
        }
    }

    fun stopService(context: Context, serviceClass: Class<*>) {
        if (!isMyServiceRunning(context, serviceClass)) return
        val service = Intent(context, serviceClass)
        context.stopService(service)
    }
}