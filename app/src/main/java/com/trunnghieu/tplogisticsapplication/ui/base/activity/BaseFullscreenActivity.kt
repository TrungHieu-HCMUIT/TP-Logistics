package com.trunnghieu.tplogisticsapplication.ui.base.activity

import androidx.databinding.ViewDataBinding
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.SystemHelper

abstract class BaseFullscreenActivity<T : ViewDataBinding, VM : BaseViewModel> :
    BaseActivity<T, VM>() {

    private val runInFullScreen = false

    override fun onStart() {
        super.onStart()
        if (runInFullScreen) {
            SystemHelper.hideSystemUI(window)
        }
    }
}