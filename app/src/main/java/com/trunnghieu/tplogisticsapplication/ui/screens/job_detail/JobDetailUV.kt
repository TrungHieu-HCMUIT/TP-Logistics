package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail

import androidx.fragment.app.Fragment
import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

interface JobDetailUV : BaseUserView {
    fun showDeliveryLocation()
    fun showDischargeMaterial(showJobCompleteButton: Boolean)
    fun replaceRootFragment(fragment: Fragment)
}