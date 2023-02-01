package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_material

import android.graphics.Bitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.trunnghieu.tplogisticsapplication.data.repository.local.job.LocalJob
import com.trunnghieu.tplogisticsapplication.data.repository.remote.BaseRepoCallback
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.DeliveryWorkFlowRepo
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.data.repository.remote.work_flow_service.WorkFlowRepo
import com.trunnghieu.tplogisticsapplication.ui.base.BaseRepoViewModel

class PickupMaterialVM : BaseRepoViewModel<WorkFlowRepo, PickupMaterialUV>() {

    private val driverJobRepo = DeliveryWorkFlowRepo()

    override fun createRepo(): WorkFlowRepo {
        return WorkFlowRepo()
    }

    var isConnected = true
    private var needSubmitArriveAtPickupLocation = false

    // Job data
    var latestJob: Job? = null
    private var qrBitmap: Bitmap? = null
    private var refreshQRJob: kotlinx.coroutines.Job? = null

    // Conditions
    private var isTripBased = true
    private var hasPickupQR = false
    private var pickUpQRScanned = false
//    TODO: UNCOMMENT private var doReminder = false
    private var doReminder = true
    private var didTonnageSubmissionLocationIsPickup = false
    private var didTonnageSubmissionTypeIsManual = true
    val netWeight = MutableLiveData(0.0)
    var unLandedVehicleWeight = 0.0
    private var eSignAvailable = false
//    TODO: UNCOMMENT private var eSignRequired = false
    private var eSignRequired = true
    var needSubmitPickupDone = false

    // [VERSION 1.6] Pickup Location = Unload Location
    var isPickupEqualUnloadLocation = false
    var isConfirmWorking = false
    val showWorkingScreen = MutableLiveData<Boolean>()

    // UI
    private var showWeightData = false
    private var showInputNetWeight = false
    private var showNetWeightHighlighted = false
    private var showEditNetWeight = false
    private var showActionBtn = false
    private var actionForPickupDone = true

    /**
     * Init life cycle
     */
    fun initLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    /**
     * Show local job data
     * In case no-connectivity
     */
    fun showLocalJob() {
        val latestJob = LocalJob.get().getLatestJob() ?: return
        jobLogicForPickupMaterial(latestJob)
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
//                handleSubmitAtPickupLocation(data.job)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    private fun handleSubmitAtPickupLocation(availableJob: Job) {
//        needSubmitArriveAtPickupLocation =
//            ApiConst.JobStatus.valueOf(availableJob.jobStatus) == ApiConst.JobStatus.DRIVER_JOB_STARTED
//        if (needSubmitArriveAtPickupLocation) {
//            CustomLogger.e("Need submit arrive at Pickup Location")
//            repo?.submitPickupArrive(apiCallback)
//        } else {
//            getJobDataAtPickupMaterial()
//        }
    }

    private fun getJobDataAtPickupMaterial() {
//        needSubmitArriveAtPickupLocation = false
//        latestJob = LocalJob.get().getLatestJob() ?: return
//        isTripBased = latestJob!!.tripBase
//        unLandedVehicleWeight = latestJob!!.unladenWeight
//        if (isTripBased) {
//            repo?.getJobAtPickupDone(callback = apiCallback)
//        } else {
//            if (latestJob!!.hasPickupQr) {
//                repo?.getJobAtPickupDone(callback = apiCallback)
//            } else {
//                repo?.getJobAtPickupTonnage(callback = apiCallback)
//            }
//        }
    }

    private val apiCallback = object : BaseRepoCallback<Job> {
        override fun apiRequesting(showLoading: Boolean) {
            if (!needSubmitArriveAtPickupLocation) {
                if (!showLoading) showLoading(false)
            }
        }

        override fun apiResponse(data: Job) {
            latestJob = data
            if (needSubmitArriveAtPickupLocation) {
                getJobDataAtPickupMaterial()
                return
            }
            jobLogicForPickupMaterial(latestJob!!)
        }

        override fun showMessage(message: String?) {
            showError(message)
        }
    }

    private fun jobLogicForPickupMaterial(job: Job) {
//        isTripBased = job.tripBase
//        hasPickupQR = job.hasPickupQr
//        pickUpQRScanned = job.pickUpQRScanned
//        doReminder = job.doReminder
//        eSignAvailable = job.pickupESignature == true
//        eSignRequired = eSignAvailable && latestJob?.dispatcherName?.isEmpty() == true
//        isPickupEqualUnloadLocation = job.isPickupEqualUnloadLocation()
//
//        val qrString = job.pickUpQr
//        if (qrString.isNotEmpty()) {
//            val qrCodeSize = GoTruckApp.instance.applicationContext.resources.getDimension(
//                R.dimen.qr_image_big
//            ).toInt()
//            qrBitmap = QRCode.from(qrString)
//                .withSize(qrCodeSize, qrCodeSize)
//                .withHint(EncodeHintType.MARGIN, 0)
//                .bitmap()
//        } else {
//            pickUpQRScanned = true
//        }
//
//        stopRefreshScanStatus()
//        if (isTripBased) {
//            CustomLogger.e("TRIP BASED")
//            showWeightData = false
//            showInputNetWeight = false
//            showNetWeightHighlighted = false
//            CustomLogger.d("hasPickupQR: $hasPickupQR -- pickupQRScanned: $pickUpQRScanned")
//            if (hasPickupQR && !pickUpQRScanned) {
//                showActionBtn = false
//                refreshScanStatus()
//            } else {
//                showActionBtn = true
//            }
//        } else {
//            CustomLogger.e("TON BASED")
//            didTonnageSubmissionLocationIsPickup = job.didTonnageSubmissionLocationIsPickup()
//            didTonnageSubmissionTypeIsManual = job.didTonnageSubmissionTypeIsManual()
//            CustomLogger.d(
//                "didTonnageSubmissionLocationIsPickup: $didTonnageSubmissionLocationIsPickup"
//                        + "\n" +
//                        "didTonnageSubmissionTypeIsManual: $didTonnageSubmissionTypeIsManual"
//                        + "\n" +
//                        "hasPickupQR: $hasPickupQR -- pickUpQRScanned: $pickUpQRScanned"
//                        + "\n" +
//                        "jobStatus: ${job.jobStatus} "
//            )
//            if (hasPickupQR) {
//                // HAS PICKUP QR
//                showEditNetWeight = false
//                actionForPickupDone = true
//                if (didTonnageSubmissionLocationIsPickup) {
//                    showActionBtn = pickUpQRScanned
//                    if (pickUpQRScanned) {
//                        // Show Laden & Net weight from API
//                        showWeightData = true
//                        showInputNetWeight = false
//                        showNetWeightHighlighted = true
//                    } else {
//                        // Refresh API every 3s to check pickupQRScanned
//                        refreshScanStatus()
//                    }
//                } else {
//                    showActionBtn = pickUpQRScanned
//                    if (pickUpQRScanned) {
//                        // Show Net weight = est 17,500 kg
//                        showWeightData = true
//                        showInputNetWeight = false
//                        showNetWeightHighlighted = true
//                    } else {
//                        // Refresh API every 3s to check pickupQRScanned
//                        refreshScanStatus()
//                    }
//                }
//            } else {
//                // HAS NO PICKUP QR
//                showEditNetWeight = true
//                showActionBtn = true
//                if (didTonnageSubmissionLocationIsPickup) {
//                    if (didTonnageSubmissionTypeIsManual) {
//                        if (job.getNetWeightAtPickup() == 0.0) {
//                            // Show input & submit Net weight
//                            actionForPickupDone = false
//                            doReminder = false
//                            showWeightData = false
//                            showInputNetWeight = true
//                            showNetWeightHighlighted = false
//                        } else {
//                            // Already submit net weight. Not show input net weight
//                            actionForPickupDone = true
//                            doReminder = job.doReminder
//                            showWeightData = true
//                            showInputNetWeight = false
//                            showNetWeightHighlighted = true
//                        }
//                    }
//                } else {
//                    // Show Net weight = est 17,500 kg
//                    actionForPickupDone = true
//                    doReminder = job.doReminder
//                    showWeightData = true
//                    showInputNetWeight = false
//                    showNetWeightHighlighted = true
//                    showEditNetWeight = false
//                }
//            }
//        }
//
//        uiCallback?.run {
//            displayQR(hasPickupQR, pickUpQRScanned, qrBitmap)
//            showDoReminder(isTripBased, doReminder, pickUpQRScanned)
//            showWeight(
//                showWeightData,
//                showInputNetWeight,
//                showNetWeightHighlighted,
//                showEditNetWeight
//            )
//            showWorkingScreen.value = false
//            showBtnAction(showActionBtn, actionForPickupDone)
//            updateLatestJob(job)
//        }
//
//        // After upload eSign, refresh job data and continue submit Pickup Done
//        if (needSubmitPickupDone) {
//            needSubmitPickupDone = false
//            submitPickupDone()
//        }
    }

    /**
     * Refresh API to get scan status
     */
    private fun refreshScanStatus() {
//        refreshQRJob = CoroutineScope(Dispatchers.IO).launch {
//            delay(GoTruckConst.JOB.TIME_REFRESH_QR)
//            if (!isConnected) {
//                refreshScanStatus()
//                return@launch
//            }
//
//            withContext(Dispatchers.Main) {
//                if (pickUpQRScanned) {
//                    stopRefreshScanStatus()
//                }
//                getJobDataAtPickupMaterial()
//            }
//        }
//        refreshQRJob!!.start()
    }

    /**
     * Rescan QR Code
     */
    fun rescanQR() {
//        if (!hasPickupQR) return
//        stopRefreshScanStatus()
//        repo?.reScanQRAtPickup(object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                latestJob = data
//                showWeightData = false
//                showNetWeightHighlighted = false
//                showActionBtn = false
//                jobLogicForPickupMaterial(latestJob!!)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    /**
     * Stop refresh scan status
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopRefreshScanStatus() {
        refreshQRJob?.cancel()
        refreshQRJob = null
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
//                showActionBtn = false
//                jobLogicForPickupMaterial(latestJob!!)
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    /**
     * Submit action for pickup material
     */
    fun submitPickupMaterial() {
        if (actionForPickupDone) {
            uiCallback?.confirmPickupDone(
                isTripBased,
                doReminder,
                eSignAvailable,
                didTonnageSubmissionLocationIsPickup
            )
            return
        }
        uiCallback?.confirmNetWeight()
    }

    // [VERSION 1.6] Pickup Location = Unload Location
    /**
     * Clicked on CONTINUE button -> Submit pickup done
     */
    fun confirmWorkingAndSubmitPickupDone() {
        isConfirmWorking = true
        submitPickupDone()
    }

    /**
     * Submit weight
     */
    fun submitWeight() {
//        stopRefreshScanStatus()
//        repo?.submitPickupTonnage(
//            RequestTonnageJobDTO(netWeight.value ?: 0.0),
//            object : BaseRepoCallback<Job> {
//                override fun apiRequesting(showLoading: Boolean) {
//                    showLoading(showLoading)
//                }
//
//                override fun apiResponse(data: Job) {
//                    latestJob = data
//                    jobLogicForPickupMaterial(latestJob!!)
//                }
//
//                override fun showMessage(message: String?) {
//                    showError(message)
//                }
//            }
//        )
    }

    /**
     * Submit pickup done status
     */
    fun submitPickupDone() {
        stopRefreshScanStatus()
        if (checkExtraCasesAtPickup()) return
//        repo?.submitPickupDone(callback = object : BaseRepoCallback<Job> {
//            override fun apiRequesting(showLoading: Boolean) {
//                showLoading(showLoading)
//            }
//
//            override fun apiResponse(data: Job) {
//                uiCallback?.updateLatestJob(data)
//                uiCallback?.pickupDone(ApiConst.JobStatus.valueOf(data.jobStatus))
//            }
//
//            override fun showMessage(message: String?) {
//                showError(message)
//            }
//        })
    }

    private fun checkExtraCasesAtPickup(): Boolean {
        if (eSignRequired) {
            uiCallback?.showESignAtPickup()
            return true
        }

        if (doReminder) {
            // Physical DO
            return false
        }

        // [VERSION 1.6] Pickup Location = Unload Location
        if (isPickupEqualUnloadLocation && !isConfirmWorking) {
            showWorkingScreen.value = true
            uiCallback?.run {
                showWorkingScreen()
                showBtnAction(showActionBtn = false, actionForPickupDone = true)
            }
            return true
        }

        return false
    }
}
