package com.trunnghieu.tplogisticsapplication

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.trunnghieu.tplogisticsapplication.utils.CustomLogger
import com.trunnghieu.tplogisticsapplication.utils.NotificationUtils
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

/**
 * Created on 2021/04/12 - 16:38
 */
class TPLogisticsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext

        // Logger
        Logger.addLogAdapter(AndroidLogAdapter())
        // Preferences
        AppPreferences.get().init(this)
        // Notification
        NotificationUtils.createNotificationChannel(this, CHANNEL_ID, CHANNEL_NAME)
        // Custom Font
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/sfprodisplay_*.ttf")
                            .build()
                    )
                )
                .build()
        )

        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleListener())
    }

    class AppLifecycleListener : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            CustomLogger.e("[LIFE_CYCLE] App moved to foreground")
//            FAnalytics.logEvent(
//                FAnalytics.EVENT_APP_RUNNING_AT_BACKGROUND,
//                FAnalytics.Analytic(
//                    FAnalytics.FIELD_AT_BACKGROUND, "false"
//                )
//            )
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            CustomLogger.e("[LIFE_CYCLE] App move to background")
//            FAnalytics.logEvent(
//                FAnalytics.EVENT_APP_RUNNING_AT_BACKGROUND,
//                FAnalytics.Analytic(
//                    FAnalytics.FIELD_AT_BACKGROUND, "true"
//                )
//            )
        }
    }

    companion object {
        lateinit var instance: TPLogisticsApp
            private set
        lateinit var appContext: Context

        const val PACKAGE_NAME = BuildConfig.APPLICATION_ID
        const val CHANNEL_ID = "$PACKAGE_NAME.tplogistics_2022"
        const val CHANNEL_NAME = "$PACKAGE_NAME.tplogistics"
    }

}