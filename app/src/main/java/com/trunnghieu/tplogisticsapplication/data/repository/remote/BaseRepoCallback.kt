package com.trunnghieu.tplogisticsapplication.data.repository.remote

interface BaseRepoCallback<T> {
    fun apiRequesting(showLoading: Boolean) = Unit
    fun apiResponse(data: T)
    fun showMessage(message: String?) = Unit
    fun connectionError() = Unit
}