package com.trunnghieu.tplogisticsapplication.ui.base.viewmodel

import com.trunnghieu.tplogisticsapplication.ui.base.BaseUserView

abstract class BaseUiViewModel<V: BaseUserView>: BaseViewModel() {

    protected var uiCallback: V? = null

    fun init(uiCallback: V) {
        this.uiCallback = uiCallback
    }

}