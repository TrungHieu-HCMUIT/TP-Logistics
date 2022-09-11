package com.trunnghieu.tplogisticsapplication.data.repository.remote

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.trunnghieu.tplogisticsapplication.TPLogisticsApp
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.api.base.BaseApi
import com.trunnghieu.tplogisticsapplication.data.filter.BroadCastFilter
import com.trunnghieu.tplogisticsapplication.utils.CustomLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.UnknownHostException

abstract class BaseRepo : BaseApi() {

    protected fun <T> getApiCallback(callback: BaseRepoCallback<T>): Callback<T> {
        callback.apiRequesting(true)
        return object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                callback.run {
                    apiRequesting(false)
                    getBodyResponse(response)?.let {
                        apiResponse(it)
                        return
                    }
                    handleErrorResponse(response, callback)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.run {
                    apiRequesting(false)
                    if (t is UnknownHostException) {
                        connectionError()
                        return
                    }
                    showMessage(t.message)
                }
            }
        }
    }

    protected fun <T> getBodyResponse(response: Response<T>): T? {
        if (response.isSuccessful) {
            val bodyResponse = response.body()
            bodyResponse?.let { body ->
                return body
            }
        }
        return null
    }

    protected fun <T> handleErrorResponse(response: Response<T>, callback: BaseRepoCallback<T>) {
        when (response.code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorMessage = getErrMessage(response)
                if (errorMessage == ApiConst.Error.MESSAGE_UNPAIRED_TRUCK) {
                    CustomLogger.e("There is no vehicle or vehicle In-Active")
                    TPLogisticsApp.instance.applicationContext
                        .sendBroadcast(Intent(BroadCastFilter.ACTION_UNPAIRED_TRUCK))
                    return
                }
                if (errorMessage.contains(ApiConst.Error.MSG_LOGIN_ON_ANOTHER_DEVICE)) {
                    CustomLogger.e("This account already logged in on another device")
                    TPLogisticsApp.instance.applicationContext
                        .sendBroadcast(Intent(BroadCastFilter.ACTION_ACCOUNT_CONFLICT))
                } else {
                    callback.showMessage(errorMessage)
                }
            }
            ApiConst.HTTP_CODE_UPGRADE_REQUIRED -> {
                CustomLogger.e("Update available")
                LocalBroadcastManager.getInstance(TPLogisticsApp.appContext)
                    .sendBroadcast(Intent(BroadCastFilter.ACTION_UPDATE_AVAILABLE))
            }
            else -> {
                callback.showMessage(getErrMessage(response))
            }
        }
    }
}