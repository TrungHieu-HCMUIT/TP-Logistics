package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.esign

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.preferences.AppPrefs
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.DeliveryWorkFlowRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel
import com.trunnghieu.tplogisticsapplication.utils.helper.AppPreferences
import com.trunnghieu.tplogisticsapplication.utils.helper.FileHelper
import java.io.File

class ESignVM : BaseRepoViewModel<DeliveryWorkFlowRepo, ESignUV>() {

    override fun createRepo(): DeliveryWorkFlowRepo {
        return DeliveryWorkFlowRepo()
    }

    // Preferences
    private val appPrefs = AppPreferences.get()

    val forPickup = MutableLiveData(true)

    val name = MutableLiveData("")
    val remark = MutableLiveData("")
    val signatureAvailable = MutableLiveData<Boolean>()
    private var signatureFile: File? = null

    /**
     * Get extras data from bundle
     */
    fun getExtras(arguments: Bundle?) {
        arguments?.let {
            forPickup.value = it.getBoolean(ESignDialog.ARG_AT_PICKUP)
        }
    }

    /**
     * Clear signature
     */
    fun clearSignature() {
        uiCallback?.clearSignature()
    }

    /**
     * Save signature bitmap to file
     */
    fun saveSignatureFile(context: Context, signatureBitmap: Bitmap) {
        signatureFile = FileHelper.bitmapToFile(context, signatureBitmap)
    }

    /**
     * Validate input & signature to enable Confirm button
     */
    fun validateToEnableConfirm() {
        val isNameNotEmpty = name.value?.isNotEmpty() == true
        val isSignatureSigned = signatureAvailable.value == true
        uiCallback?.enableConfirm(isNameNotEmpty && isSignatureSigned)
    }

    /**
     * Submit signature + name + remark
     */
    fun submitSignature(job: Job) {
        signatureFile ?: return
//        repo?.uploadESignature(
//            signatureFile!!,
//            appPrefs.getString(AppPrefs.LOGIN.PHONE_NUMBER),
//            appPrefs.getString(AppPrefs.LOGIN.DEVICE_ID),
//            name.value ?: "",
//            remark.value ?: "",
//            job.orgCode,
//            job.bookingNo,
//            job.load          No,
//            forPickup.value ?: true,
//            object : BaseProgressCallback<Job> {
//                override fun showProgress(show: Boolean) {
//                    uiCallback?.showProgress(show)
//                }
//
//                override fun progressUpdate(progress: Int) {
//                    uiCallback?.progressUpdate(progress)
//                }
//
//
//                override fun apiResponse(data: Job) {
//                    uiCallback?.updateLatestJob(data)
                    uiCallback?.uploadESignSuccess()
//                }
//
//                override fun showMessage(message: String?) {
//                    showError(message)
//                }
//            }
//        )
    }

    /**
     * Cancel E-Sign and dismiss popup
     */
    fun cancelESign() {
        uiCallback?.cancelESign()
    }
}
