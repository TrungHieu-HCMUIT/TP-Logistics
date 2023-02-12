package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.discharge_material

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.databinding.FragmentDischargeMaterialBinding
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseLocationFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.esign.ESignDialog

class DischargeMaterialFragment :
    BaseLocationFragment<FragmentDischargeMaterialBinding, DischargeMaterialVM>(),
    DischargeMaterialUV {

    companion object {
        const val ARG_SHOW_JOB_COMPLETE_BUTTON = "ARG_SHOW_JOB_COMPLETE_BUTTON"
        const val ARG_LADEN_WEIGHT_FROM_PICKUP = "ARG_LADEN_WEIGHT_FROM_PICKUP"
        const val ARG_NET_WEIGHT_FROM_PICKUP = "ARG_NET_WEIGHT_FROM_PICKUP"

        fun newInstance(
            showJobCompleteButton: Boolean,
            ladenWeightFromPickup: Double,
            netWeightFromPickup: Double
        ) = DischargeMaterialFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_SHOW_JOB_COMPLETE_BUTTON, showJobCompleteButton)
                putDouble(ARG_LADEN_WEIGHT_FROM_PICKUP, ladenWeightFromPickup)
                putDouble(ARG_NET_WEIGHT_FROM_PICKUP, netWeightFromPickup)
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_discharge_material

    override fun viewModelClass(): Class<DischargeMaterialVM> {
        return DischargeMaterialVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: DischargeMaterialVM) {
        viewModel.run {
            init(this@DischargeMaterialFragment)
            initLifeCycle(lifecycle)
        }
        binding.apply {
            vm = viewModel
            jobVM = this@DischargeMaterialFragment.jobVM
        }
    }

    fun getViewModel() = viewModel

    private var executedApi = false

    // E-Sign
    var eSignDialog: ESignDialog? = null

    // [VERSION 1.6] For Working Time
//    var workingTimeDialog: WorkingTimeDialog? = null

    override fun initView() {
        //TODO: Fake
    }

    override fun initData() {
        viewModel.getExtras(arguments)
        viewModel.showBtnAction()

        // Observer connectivity
//        jobVM.isLocationOrInternetConnected.observe(this) { isConnected ->
//            viewModel.isConnected = isConnected
//
//            if (!isConnected) {
//                executedApi = false
//                viewModel.showLocalJob()
//                return@observe
//            }
//
//            if (!executedApi) {
//                executedApi = true
//                showLoading(true)
//                viewModel.getAvailableJob()
//            }
//        }
    }

    override fun onLocationPermissionGranted() {
        binding.run {
            // Get net weight
//            viewInputTonnage.edtNetWeight.addTextChangedListener {
//                val netWeight = it?.toString()?.toDoubleOrNull() ?: 0.0
//                viewModel.netWeight.value = netWeight
//                val validNetWeight = viewModel.latestJob?.validNetWeight(netWeight) == true
//                btnSubmit.isEnabled = validNetWeight
//                viewInputTonnage.showValidate = !validNetWeight
//                updateInputUIForValidWeight(validNetWeight, viewInputTonnage.tilNetWeight)
//            }
        }
    }

    override fun jobIsNotAvailable() {
        showMessage(fragmentContext.getString(R.string.start_work_msg_no_job_available))
    }

    override fun showDoReminder(isTripBased: Boolean, hasPickupDoReminder: Boolean) {
        binding.run {
            viewDoReminder.apply {
                visibility = if (hasPickupDoReminder) View.VISIBLE else View.GONE
                text = if (isTripBased) {
                    fragmentContext.getString(R.string.job_detail_msg_issue_or_collect_do)
                } else {
                    fragmentContext.getString(R.string.job_detail_msg_get_the_do_signed)
                }
            }
            executePendingBindings()
        }
    }

    override fun showWeight(
        showWeightData: Boolean,
        showInputNetWeight: Boolean,
        showNetWeightHighlighted: Boolean,
        showEditNetWeight: Boolean
    ) {
        binding.apply {
            viewJobDetail.showWeightData = showWeightData
            viewInputTonnage.root.visibility = if (showInputNetWeight) View.VISIBLE else View.GONE
            viewNetWeight.apply {
                root.visibility =
                    if (showNetWeightHighlighted) View.VISIBLE else View.GONE
                showEdit = showEditNetWeight
            }
            executePendingBindings()
        }
    }

    override fun showLadenAndNetWeightFromPickup(
        ladenWeightFromPickup: Double,
        netWeightFromPickup: Double
    ) {
//        jobVM.latestJob.value?.apply {
//            landenWeight = ladenWeightFromPickup
//            actualWeightKg = netWeightFromPickup
//        }
    }

    override fun showBtnAction(
        showSubmitBtn: Boolean,
        showActionBtn: Boolean,
        showDischargeBtn: Boolean,
        actionForJobComplete: Boolean
    ) {
        binding.apply {
            btnSubmit.visibility = View.GONE
            btnJobComplete.visibility = View.GONE
            btnDischarge.visibility = View.GONE
            btnScanDo.visibility = View.GONE
            if (showSubmitBtn) {
                btnSubmit.visibility = View.VISIBLE
                btnSubmit.isEnabled = false
            } else {
                if (!showActionBtn) return
                if (actionForJobComplete) {
                    showDischargeData = false
                    if (showDischargeBtn) {
                        btnDischarge.visibility = View.VISIBLE
                    } else {
                        btnJobComplete.visibility = View.VISIBLE
                    }
                } else {
                    showDischargeData = true
                    btnScanDo.visibility = View.VISIBLE
                }
            }
            executePendingBindings()
        }
    }

    override fun confirmNetWeight() {
        showAlert(
            fragmentContext.getString(R.string.job_detail_msg_confirm_net_weight),
            fragmentContext.getString(R.string.alert_btn_yes),
            fragmentContext.getString(R.string.alert_btn_no),
            object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    viewModel.submitWeight()
                }

                override fun onNegativeClick() {

                }
            }
        )
    }

    override fun resetWeight() {
        binding.viewInputTonnage.edtNetWeight.setText("")
    }

    override fun showConfirmJob(
        isTripBased: Boolean,
        doReminder: Boolean,
        eSignAvailable: Boolean
    ) {
        if (doReminder) {
            showAlert(
                fragmentContext.getString(R.string.job_detail_msg_confirm_complete_at_discharge_material_ton_based),
                fragmentContext.getString(R.string.alert_btn_yes),
                fragmentContext.getString(R.string.alert_btn_no),
                object : AppAlertDialog.AlertDialogOnClickListener {
                    override fun onPositiveClick() {
                        viewModel.submitJobComplete()
                    }
                }
            )
        } else {
            viewModel.submitJobComplete()
        }
    }

    override fun startScanDO() {
//        requestPermission(
//            fragmentContext.getString(R.string.permission_msg),
//            listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
//            object : BaseActivity.RequestPermissionCallback {
//                override fun onPermissionGranted() {
//                    navigator.rootFragment = ScanDoFragment()
//                }
//
//                override fun onPermissionDenied() {
//                    onFragmentBackPressed()
//                }
//            }
//        )
    }

    override fun updateLatestJob(latestJob: Job) {
        jobVM.latestJob.value = latestJob
    }

    override fun dischargeDone(jobStatus: ApiConst.JobStatus) {
        jobVM.run {
            changeJobStatus(jobStatus)

            // [GOT-450] Disable highlight job when change other job screen
            showHighlightDataChanged(show = false, disableImmediately = true)
        }
    }

    override fun showESignAtDischarge() {
        eSignDialog = ESignDialog.show(parentFragmentManager, jobVM, false) {
            viewModel.run {
                needSubmitJobComplete = true
                eSignRequired = false
                alreadySubmitWorkingTime = true
                getAvailableJob()
                showJobCompleteAction()
            }
        }
    }

    override fun showWorkingTimeAtDischarge() {
        getBaseActivity()?.let {
//            workingTimeDialog = WorkingTimeDialog.show(it, jobVM) {
//                viewModel.run {
//                    alreadySubmitWorkingTime = true
//                    needSubmitJobComplete = true
//                    getAvailableJob()
//                }
//            }
        }
    }
}
