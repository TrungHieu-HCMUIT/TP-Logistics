package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.pickup_material

import android.view.View
import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.data.api.ApiConst
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.databinding.FragmentPickupMaterialBinding
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseLocationFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.JobDetailVM
import com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.esign.ESignDialog

class PickupMaterialFragment(private val jobDetailVM: JobDetailVM? = null) :
    BaseLocationFragment<FragmentPickupMaterialBinding, PickupMaterialVM>(),
    PickupMaterialUV {

    override fun layoutRes() = R.layout.fragment_pickup_material

    override fun viewModelClass(): Class<PickupMaterialVM> {
        return PickupMaterialVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: PickupMaterialVM) {
        viewModel.run {
            init(this@PickupMaterialFragment)
            initLifeCycle(lifecycle)
        }
        binding.apply {
            vm = viewModel
            jobVM = this@PickupMaterialFragment.jobVM
        }
    }

    fun getViewModel() = viewModel

    private var executedApi = false

    // E-Sign
    var eSignDialog: ESignDialog? = null

    override fun initView() {

    }

    override fun initData() {
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
//                btnPickupMaterialAction.isEnabled = validNetWeight
//                viewInputTonnage.showValidate = !validNetWeight
//                updateInputUIForValidWeight(validNetWeight, viewInputTonnage.tilNetWeight)
//            }
        }
    }

    override fun jobIsNotAvailable() {
        showMessage(fragmentContext.getString(R.string.start_work_msg_no_job_available))
    }

    override fun showDoReminder(
        isTripBased: Boolean,
        hasPickupDoReminder: Boolean,
        pickUpQRScanned: Boolean
    ) {
        binding.run {
            viewDoReminder.apply {
                visibility = if (hasPickupDoReminder) View.VISIBLE else View.GONE
                text = if (isTripBased) {
                    fragmentContext.getString(R.string.job_detail_msg_issue_or_collect_do)
                } else {
                    if (!pickUpQRScanned) {
                        fragmentContext.getString(R.string.job_detail_msg_issue_or_collect_do)
                    } else {
                        fragmentContext.getString(R.string.job_detail_msg_collect_do)
                    }
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

    override fun showBtnAction(showActionBtn: Boolean, actionForPickupDone: Boolean) {
        binding.apply {
            btnPickupMaterialAction.visibility =
                if (showActionBtn)
                    View.VISIBLE
                else
                    View.GONE
            btnPickupMaterialAction.text =
                if (actionForPickupDone) {
                    fragmentContext.getString(R.string.job_detail_btn_pickup_done)
                } else {
                    fragmentContext.getString(R.string.job_detail_btn_submit)
                }
            btnPickupMaterialAction.isEnabled = actionForPickupDone
            executePendingBindings()
        }
    }

    override fun resetWeight() {
        binding.viewInputTonnage.edtNetWeight.setText("")
    }

    override fun confirmPickupDone(
        isTripBased: Boolean,
        doReminder: Boolean,
        eSignAvailable: Boolean,
        didTonnageSubmissionLocationIsPickup: Boolean
    ) {
        if (doReminder) {
            showAlert(
                fragmentContext.getString(R.string.job_detail_msg_confirm_pickup_done_at_pickup_material),
                fragmentContext.getString(R.string.alert_btn_yes),
                fragmentContext.getString(R.string.alert_btn_no),
                object : AppAlertDialog.AlertDialogOnClickListener {
                    override fun onPositiveClick() {
                        viewModel.submitPickupDone()
                    }
                }
            )
        } else {
            viewModel.submitPickupDone()
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

    override fun updateLatestJob(latestJob: Job) {
        jobVM.latestJob.value = latestJob
    }

    override fun showESignAtPickup() {
        eSignDialog = ESignDialog.show(parentFragmentManager, jobVM, true) {
            viewModel.run {
                needSubmitPickupDone = true
                getAvailableJob()
            }
            jobVM.run {
                changeJobStatus(ApiConst.JobStatus.DRIVER_PICKUP_DONE)
            }
        }
    }

    override fun showWorkingScreen() {
        jobDetailVM?.jobTitle?.value = fragmentContext.getString(R.string.working_title)
    }

    override fun pickupDone(jobStatus: ApiConst.JobStatus) {
        jobVM.run {
            changeJobStatus(jobStatus)

            // [GOT-450] Disable highlight job when change other job screen
            showHighlightDataChanged(show = false, disableImmediately = true)
        }
    }
}
