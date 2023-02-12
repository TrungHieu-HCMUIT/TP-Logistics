package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.discharge_material

import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepoCallback
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.DeliveryWorkFlowRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.data.repository.remote.work_flow_service.WorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel

class DischargeMaterialVM : BaseRepoViewModel<WorkFlowRepo, DischargeMaterialUV>() {

    private val driverJobRepo = DeliveryWorkFlowRepo()

    override fun createRepo(): WorkFlowRepo {
        return WorkFlowRepo()
    }

    var isConnected = true
    private var needSubmitArriveAtDeliveryLocation = false

    // Job data
    var latestJob: Job? = null
    private var qrBitmap: Bitmap? = null
    private var refreshQRJob: kotlinx.coroutines.Job? = null

    // Conditions
    private var isTripBased = true
    private var hasDeliveryQR = false
    private var deliveryQRScanned = false

    //    private var doReminder = false
    private var doReminder = true
    private var didTonnageSubmissionLocationIsDischarge = false
    private var didTonnageSubmissionTypeIsManual = true
    private var ladenWeightFromPickup = 0.0
    private var netWeightFromPickup = 0.0
    val netWeight = MutableLiveData(0.0)
    private var deliveryESignAvailable = false
    private var eSignRequired = true
    var editWorkingTimeRequired = false
    var alreadySubmitWorkingTime = false
    var needSubmitJobComplete = false

    // UI
    private var showWeightData = false
    private var showInputNetWeight = false
    private var showNetWeightHighlighted = false
    private var showEditNetWeight = false
    private var showActionBtn = true
    private var showSubmitBtn = false
    private var showDischargeBtn = true
    private var showJobCompleteBtn = true
    private var actionForJobComplete = true

    /**
     * Init life cycle
     */
    fun initLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    /**
     * Get extras data from bundle
     */
    fun getExtras(arguments: Bundle?) {
        arguments ?: return
        showJobCompleteBtn =
            arguments.getBoolean(DischargeMaterialFragment.ARG_SHOW_JOB_COMPLETE_BUTTON)
        ladenWeightFromPickup =
            arguments.getDouble(DischargeMaterialFragment.ARG_LADEN_WEIGHT_FROM_PICKUP)
        netWeightFromPickup =
            arguments.getDouble(DischargeMaterialFragment.ARG_NET_WEIGHT_FROM_PICKUP)
        actionForJobComplete = showJobCompleteBtn
    }

    /**
     * Show local job data
     * In case no-connectivity
     */
    fun showLocalJob() {
        val latestJob = LocalJob.get().getLatestJob() ?: return
        jobLogicForDischargeMaterial(latestJob)
    }

    /**
     * Get job data after pickup done
     */
    fun getAvailableJob() {
//        driverJobRepo.getAvailableJob(object : BaseRepoCallback<AvailableJob> {
//            override fun apiRequesting(showLoading: Boolean) {
//            }
//
//            override fun apiResponse(data: AvailableJob) {
//                val jobAvailable = data.jobAvailable
//                if (!jobAvailable) {
//                    uiCallback?.jobIsNotAvailable()
//                    return
//                }
//                handleSubmitAtDeliveryLocation(data.job)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    private fun handleSubmitAtDeliveryLocation(availableJob: Job) {
//        needSubmitArriveAtDeliveryLocation =
//            ApiConst.JobStatus.valueOf(availableJob.jobStatus) == ApiConst.JobStatus.DRIVER_PICKUP_DONE
//        if (needSubmitArriveAtDeliveryLocation) {
//            CustomLogger.e("Need submit arrive at Delivery Location")
//            repo?.submitDeliveryArrive(apiCallback)
//        } else {
//            getJobDataAtDischargeMaterial()
//        }
    }

    private fun getJobDataAtDischargeMaterial() {
//        CustomLogger.e("Get job data at Discharge Material")
//        needSubmitArriveAtDeliveryLocation = false
//        latestJob = LocalJob.get().getLatestJob() ?: return
//        isTripBased = latestJob!!.tripBase
//        didTonnageSubmissionLocationIsDischarge =
//            latestJob!!.didTonnageSubmissionLocationIsDischarge()
//        if (latestJob!!.jobStatus in listOf(
//                ApiConst.JobStatus.DRIVER_DELIVERY_ARRIVED.name,
//                ApiConst.JobStatus.DRIVER_PICKUP_DONE.name,
//                ApiConst.JobStatus.DRIVER_DISCHARGE_TONNAGE_SUBMITTED.name
//            )
//        ) {
//            if (isTripBased) {
//                repo?.getJobComplete(callback = apiCallback)
//            } else {
//                if (didTonnageSubmissionLocationIsDischarge) {
//                    repo?.getJobAtDeliveryTonnage(callback = apiCallback)
//                } else {
//                    repo?.getJobComplete(callback = apiCallback)
//                }
//            }
//        } else {
//            repo?.getJobAtScanDO(callback = apiCallback)
//        }
    }

    private val apiCallback = object : BaseRepoCallback<Job> {
        override fun apiRequesting(showLoading: Boolean) {
            if (!needSubmitArriveAtDeliveryLocation) {
                if (!showLoading) {
                    showLoading(false)
                }
            }
        }

        override fun apiResponse(data: Job) {
            latestJob = data
            if (needSubmitArriveAtDeliveryLocation) {
                getJobDataAtDischargeMaterial()
                return
            }
            jobLogicForDischargeMaterial(latestJob!!)
        }

        override fun showMessage(message: String?) {
            showError(message)
        }
    }

    private fun jobLogicForDischargeMaterial(job: Job) {
//        uiCallback?.updateLatestJob(job)
//
//        isTripBased = job.tripBase
//        hasDeliveryQR = job.hasDeliveryQr
//        deliveryQRScanned = job.dischargeQRScanned
//        doReminder = job.doReminder
//        deliveryESignAvailable = job.unloadESignature == true
//        eSignRequired = deliveryESignAvailable && latestJob?.receiverName?.isEmpty() == true
//        editWorkingTimeRequired = latestJob?.needUpdateWorkingTime() == true
//
//        // Display qr code
//        val qrString = job.deliveryQr
//        if (qrString.isNotEmpty()) {
//            val qrCodeSize = GoTruckApp.instance.applicationContext.resources.getDimension(
//                R.dimen.qr_image_big
//            ).toInt()
//            qrBitmap = QRCode.from(qrString)
//                .withSize(qrCodeSize, qrCodeSize)
//                .withHint(EncodeHintType.MARGIN, 0)
//                .bitmap()
//        } else {
//            deliveryQRScanned = true
//        }
//
//        stopRefreshScanStatus()
//        if (isTripBased) {
//            CustomLogger.e("TRIP BASED")
//            showWeightData = false
//            showInputNetWeight = false
//            showNetWeightHighlighted = false
//            CustomLogger.d("hasDeliveryQR: $hasDeliveryQR -- deliveryQRScanned: $deliveryQRScanned")
//            if (hasDeliveryQR && !deliveryQRScanned) {
//                refreshScanStatus()
//            } else {
//                showActionBtn = true
//                actionForJobComplete =
//                    job.jobStatus != ApiConst.JobStatus.DRIVER_DISCHARGED.name
//            }
//        } else {
//            CustomLogger.e("TON BASED")
//            didTonnageSubmissionLocationIsDischarge = job.didTonnageSubmissionLocationIsDischarge()
//            didTonnageSubmissionTypeIsManual = job.didTonnageSubmissionTypeIsManual()
//            CustomLogger.d(
//                "didTonnageSubmissionLocationIsDischarge: $didTonnageSubmissionLocationIsDischarge"
//                        + "\n" +
//                        "didTonnageSubmissionTypeIsManual: $didTonnageSubmissionTypeIsManual"
//                        + "\n" +
//                        "hasDeliveryQR: $hasDeliveryQR -- deliveryQRScanned: $deliveryQRScanned"
//                        + "\n" +
//                        "jobStatus: ${job.jobStatus} "
//            )
//            if (hasDeliveryQR) {
//                // HAS DELIVERY QR
//                showWeightData = true
//                showEditNetWeight = false
//                showActionBtn = false
//                showNetWeightHighlighted = false
//                if (didTonnageSubmissionLocationIsDischarge) {
//                    // Tonnage Submission Location == Discharge Location
//                    // Laden weight & Net weight from API or from tonnage input
//                    // Show highlight Net weight
//                    // Show DISCHARGE button
//                    showJobCompleteBtn = false
//                    if (deliveryQRScanned) {
//                        showNetWeightHighlighted =
//                            job.jobStatus != ApiConst.JobStatus.DRIVER_DISCHARGED.name
//                        showActionBtn = true
//                        showDischargeBtn = true
//                        actionForJobComplete =
//                            job.jobStatus != ApiConst.JobStatus.DRIVER_DISCHARGED.name
//                    } else {
//                        // Refresh API every 3s to check dischargeQRScanned
//                        refreshScanStatus()
//                    }
//                } else {
//                    // Tonnage Submission Location != Discharge location
//                    // Laden & net weight not updated. Will get from Pickup
//                    // Show JOB COMPLETE button
//                    showJobCompleteBtn = true
//                    uiCallback?.showLadenAndNetWeightFromPickup(
//                        ladenWeightFromPickup,
//                        netWeightFromPickup
//                    )
//                    if (deliveryQRScanned) {
//                        // Show small QR with Hold to re-scan. Not show highlighted net weight
//                        showActionBtn = true
//                        actionForJobComplete =
//                            job.jobStatus != ApiConst.JobStatus.DRIVER_DISCHARGED.name
//                    } else {
//                        // Refresh API every 3s to check dischargeQRScanned
//                        refreshScanStatus()
//                    }
//                }
//            } else {
//                // HAS NO DELIVERY QR
//                showWeightData = true
//                showEditNetWeight = true
//                showActionBtn = true
//                actionForJobComplete = false
//                if (didTonnageSubmissionLocationIsDischarge) {
//                    if (didTonnageSubmissionTypeIsManual) {
//                        if (job.jobStatus != ApiConst.JobStatus.DRIVER_DISCHARGED.name) {
//                            if (job.actualWeightKg == 0.0) {
//                                // Show input & submit Net weight
//                                showSubmitBtn = true
//                                doReminder = false
//                                showInputNetWeight = true
//                                showNetWeightHighlighted = false
//                            } else {
//                                // Already submit net weight. Not show input net weight
//                                showSubmitBtn = false
//                                showJobCompleteBtn = true
//                                actionForJobComplete = true
//                                doReminder = job.doReminder
//                                showInputNetWeight = false
//                                showNetWeightHighlighted = true
//                            }
//                        } else {
//                            // DRIVER_DISCHARGED
//                            showSubmitBtn = false
//                            doReminder = job.doReminder
//                            showJobCompleteBtn = false
//                            actionForJobComplete = false
//                            showWeightData = true
//                            showInputNetWeight = false
//                            showNetWeightHighlighted = false
//                        }
//                    }
//                } else {
//                    // Show Net weight from API. Not show highlighted net weight
//                    showJobCompleteBtn = true
//                    actionForJobComplete =
//                        job.jobStatus != ApiConst.JobStatus.DRIVER_DISCHARGED.name
//                    showWeightData = true
//                    showInputNetWeight = false
//                    showNetWeightHighlighted = false
//                }
//            }
//        }
//
//        // Discharge button
//        showDischargeBtn = doReminder == true ||
//                (job.unloadESignature == true && job.receiverName.isEmpty())
//
//        // After upload eSign, refresh job data and continue submit Job Complete
//        if (needSubmitJobComplete) {
//            needSubmitJobComplete = false
//            submitJobComplete()
//        }
//
////        // If both Pickup & Discharge Location have E-Sign, no need to Scan DO anymore
////        val pickupESign = job.pickupESignature == true
////        if (pickupESign && deliveryESignAvailable) {
////            showJobCompleteBtn = true
////            actionForJobComplete = true
////        }
//
//        uiCallback?.run {
//            displayQR(isTripBased, hasDeliveryQR, deliveryQRScanned, qrBitmap)
//            showDoReminder(isTripBased, doReminder)
//            showWeight(
//                showWeightData,
//                showInputNetWeight,
//                showNetWeightHighlighted,
//                showEditNetWeight
//            )
//            showBtnAction(showSubmitBtn, showActionBtn, showDischargeBtn, actionForJobComplete)
//        }
    }

    fun showBtnAction() {
        uiCallback?.showBtnAction(showSubmitBtn, showActionBtn, showDischargeBtn, actionForJobComplete)
    }

    fun showJobCompleteAction() {
        showDischargeBtn = false
        uiCallback?.showBtnAction(showSubmitBtn, showActionBtn, showDischargeBtn, actionForJobComplete)
    }


    /**
     * Edit net weight
     */
    fun editNetWeight() {
//        netWeight.value = 0.0
//        uiCallback?.resetWeight()
//        repo?.resetWeight(object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                latestJob = data
//                showWeightData = false
//                showInputNetWeight = true
//                showNetWeightHighlighted = false
//                showSubmitBtn = true
//                showJobCompleteBtn = false
//                actionForJobComplete = false
//                jobLogicForDischargeMaterial(latestJob!!)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    /**
     * Confirm net weight
     */
    fun confirmNetWeight() {
        uiCallback?.confirmNetWeight()
    }

    /**
     * Submit weight
     */
    fun submitWeight() {
//        repo?.submitDeliveryTonnage(
//            RequestTonnageJobDTO(netWeight.value ?: 0.0),
//            object : BaseRepoCallback<Job> {
//                override fun apiRequesting(showLoading: Boolean) {
//                    showLoading(showLoading)
//                }
//
//                override fun apiResponse(data: Job) {
//                    latestJob = data
//                    jobLogicForDischargeMaterial(latestJob!!)
//                }
//
//                override fun showMessage(message: String?) {
//                    showError(message)
//                }
//            }
//        )
    }

    /**
     * Start scan DO
     */
    fun startScanDO() {
//        stopRefreshScanStatus()
//        uiCallback?.startScanDO()
    }

    /**
     * Request job complete
     */
    fun requestJobComplete() {
        if (showDischargeBtn) {
            uiCallback?.showConfirmJob(isTripBased, doReminder, deliveryESignAvailable)
            return
        }
        submitJobComplete()
    }

    /**
     * Submit job complete
     */
    fun submitJobComplete() {
//        stopRefreshScanStatus()
//        if (checkExtraCasesAtDischarge()) return
//        repo?.submitJobComplete(callback = object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                uiCallback?.updateLatestJob(data)
//
//                if (data.unloadESignature == true) {
//                    // E-Sign available at Discharge Material
//                    if (doReminder) {
//                        // Physical DO at Pickup, need Scan DO after Discharge
//                        uiCallback?.startScanDO()
//                        return
//                    }
//                }
//
                uiCallback?.dischargeDone(ApiConst.JobStatus.DRIVER_JOB_COMPLETED)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    private fun checkExtraCasesAtDischarge(): Boolean {
        // For E-Sign
        if (eSignRequired) {
            uiCallback?.showESignAtDischarge()
            return true
        }

        // [VERSION 1.6] For Working Time
        if (alreadySubmitWorkingTime) {
            return false
        }
        if (editWorkingTimeRequired) {
            uiCallback?.showWorkingTimeAtDischarge()
            return true
        }

        return false
    }
}
