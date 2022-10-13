package com.trunnghieu.tplogisticsapplication.utils.helper

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleHelper {

    const val SELECTED_LANGUAGE = "LocaleHelper.Selected_Language"

    fun getLanguageFromLocale(): String {
        return Locale.getDefault().language
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = AppPreferences.get().getString(SELECTED_LANGUAGE, defaultLanguage)
        return setLocale(context, lang)
    }

    fun setLocale(context: Context, language: String): Context {
        AppPreferences.get().storeValue(SELECTED_LANGUAGE, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        Locale(language)
            .run {
                Locale.setDefault(this)
                return context.createConfigurationContext(
                    createConfigurationFromLocale(
                        context,
                        this
                    )
                )
            }
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        Locale(language)
            .run {
                Locale.setDefault(this)
                context.resources.updateConfiguration(
                    createConfigurationFromLocale(context, this),
                    context.resources.displayMetrics
                )
                return context
            }
    }

    private fun createConfigurationFromLocale(context: Context, locale: Locale): Configuration {
        return context.resources.configuration
            .apply {
                setLocale(locale)
                setLayoutDirection(locale)
            }
    }
}