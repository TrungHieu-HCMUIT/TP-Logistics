package com.trunnghieu.tplogisticsapplication.ui.screens.intro

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseUiViewModel

class IntroVM : BaseUiViewModel<IntroUV>() {

    fun initLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * Start intro with delay
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startIntro() {
        uiCallback?.checkPlayServices()
    }

    /**
     * Stop intro
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopIntro() {
        uiCallback?.stopIntro()
    }
}