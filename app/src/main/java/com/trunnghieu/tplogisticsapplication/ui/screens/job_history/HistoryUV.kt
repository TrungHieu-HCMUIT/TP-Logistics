package com.trunnghieu.tplogisticsapplication.ui.screens.job_history

import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface HistoryUV : BaseUserView {
    fun onFragmentBackPressed()
    fun showDatePicker()
//    fun previewAndDownloadAllEdo(histories: List<Job>)
}