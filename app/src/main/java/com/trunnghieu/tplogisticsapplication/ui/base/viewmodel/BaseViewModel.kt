package com.trunnghieu.tplogisticsapplication.ui.base.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel: ViewModel(), LifecycleObserver {

    val viewState = SingleLiveEvent<Int>()
    var errMessage: String? = null

    fun showLoading(show: Boolean) {
        if (viewState.value == ViewState.SHOW_LOADING && show) return
        viewState.value = if (show) ViewState.SHOW_LOADING else ViewState.HIDE_LOADING
    }

    //TODO: Delete
    fun fakeShowLoading() {
        showLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            withContext(Dispatchers.Main) {
                showLoading(false)
            }
        }
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
