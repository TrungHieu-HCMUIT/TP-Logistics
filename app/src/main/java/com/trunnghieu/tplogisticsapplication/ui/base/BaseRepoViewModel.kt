package com.trunnghieu.tplogisticsapplication.ui.base

import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepo
import com.trunnghieu.tplogisticsapplication.ui.base.viewmodel.BaseViewModel

abstract class BaseRepoViewModel<T : BaseRepo, V : BaseUserView> : BaseViewModel() {

    protected abstract fun createRepo(): T

    var repo: T? = null
        get() {
            if (field == null) {
                field = createRepo()
            }
            return field
        }

    protected var uiCallback: V? = null

    fun init(uiCallback: V) {
        this.uiCallback = uiCallback
    }
}