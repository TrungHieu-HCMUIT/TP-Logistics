package com.trunnghieu.tplogisticsapplication.ui.base.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel(), LifecycleObserver {

    val viewState = SingleLiveEvent<Int>()
    var errMessage: String? = null

    fun showLoading(show: Boolean) {
        if (viewState.value == ViewState.SHOW_LOADING && show) return
        viewState.value = if (show) ViewState.SHOW_LOADING else ViewState.HIDE_LOADING
    }

    /**
     * Show error
     */
    fun showError(errMessage: String?) {
        this.errMessage = errMessage
        viewState.value = ViewState.SHOW_ERROR
    }

    /**
     * Close current screen
     */
    fun closeScreen() {
        viewState.value = ViewState.CLOSE_SCREEN
    }

}