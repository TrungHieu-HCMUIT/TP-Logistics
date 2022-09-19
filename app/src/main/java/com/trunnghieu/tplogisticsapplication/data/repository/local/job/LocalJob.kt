package com.trunnghieu.tplogisticsapplication.data.repository.local.job

import com.trunnghieu.tplogisticsapplication.data.api.helper.JsonHelper
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences

class LocalJob private constructor() {

    companion object {

        @Volatile
        private var instance: LocalJob? = null

        fun get() = instance ?: synchronized(this) {
            val newInstance = instance ?: LocalJob().also {
                instance = it
            }
            newInstance
        }
    }

    var oldJob: Job? = null

    /**
     * Save latest job to preferences
     */
    fun saveLatestJob(latestJob: Job) {
        val jobJson = JsonHelper.toString(latestJob)
        AppPreferences.get().storeValue(AppPrefs.JOB.LATEST_JOB, jobJson)
    }

    /**
     * Get latest job from preferences
     */
    fun getLatestJob(): Job? {
        val jobJson = AppPreferences.get().getString(AppPrefs.JOB.LATEST_JOB)
        if (jobJson.isNotEmpty()) {
            return JsonHelper.toObject(jobJson)
        }
        return null
    }

    /**
     * Clear latest job data
     */
    fun clearLatestJob() {
        AppPreferences.get().storeValue(AppPrefs.JOB.LATEST_JOB, null)
    }
}