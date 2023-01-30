package com.trunnghieu.tplogisticsapplication.ui.screens.job_detail.esign

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.DialogEsignBinding
import com.trunnghieu.tplogisticsapplication.data.repository.remote.delivery_workflow_service.Job
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppLoadingDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseBottomSheetDialogFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM
import com.williamww.silkysignature.views.SignaturePad


class ESignDialog(private val jobVM: JobVM, private val onSubmitted: () -> Unit) :
    BaseBottomSheetDialogFragment<DialogEsignBinding, ESignVM>(), ESignUV {

    companion object {
        const val ARG_AT_PICKUP = "ARG_AT_PICKUP"

        fun show(
            fragmentManager: FragmentManager,
            jobVM: JobVM,
            atPickup: Boolean,
            onSubmitted: () -> Unit
        ): ESignDialog {
            val eSignDialog = ESignDialog(jobVM, onSubmitted).apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_AT_PICKUP, atPickup)
                }
            }
            eSignDialog.isCancelable = false
            eSignDialog.show(fragmentManager, eSignDialog.tag)
            return eSignDialog
        }
    }

    private lateinit var eSignView: SignaturePad

    // Upload Progress
    private val progressDialog = AppLoadingDialog.get()

    override fun layoutRes() = R.layout.dialog_esign

    override fun viewModelClass() = ESignVM::class.java

    override fun initViewModel(viewModel: ESignVM) {
        viewModel.run {
            init(this@ESignDialog)
            binding.vm = viewModel
        }
        binding.jobVM = jobVM
    }

    override fun initBottomSheet() = binding.bottomSheet

    override fun initView() {
        // Signature View
        eSignView = binding.signatureView
    }

    override fun initData() {
        viewModel.getExtras(arguments)
    }

    override fun initAction() {
        // Signature view
        binding.signatureView.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {

            }

            override fun onSigned() {
                binding.btnClearSignature.isEnabled = true
                viewModel.apply {
                    signatureAvailable.value = true
                    saveSignatureFile(dialogContext, eSignView.signatureBitmap)
                    validateToEnableConfirm()
                }
            }

            override fun onClear() {
                binding.btnClearSignature.isEnabled = false
                viewModel.apply {
                    signatureAvailable.value = false
                    validateToEnableConfirm()
                }
            }
        })

        // Name
        binding.inputName.edittext.addTextChangedListener {
            val name = it?.toString()
            viewModel.apply {
                this.name.value = name
                validateToEnableConfirm()
            }
        }

        // Remark
        binding.inputRemark.edittext.addTextChangedListener {
            val remark = it?.toString()
            viewModel.remark.value = remark
        }
    }

    override fun clearSignature() {
        eSignView.clear()
    }

    override fun showProgress(show: Boolean) {
        if (show) progressDialog.showProgress()
        else progressDialog.dismiss()
    }

    override fun progressUpdate(progress: Int) {
        progressDialog.updateProgress(progress)
    }

    override fun updateLatestJob(latestJob: Job) {
        jobVM.latestJob.value = latestJob
    }

    override fun uploadESignSuccess() {
        dismiss()
        onSubmitted()
    }

    override fun cancelESign() {
        dismiss()
    }

    override fun enableConfirm(enable: Boolean) {
        binding.btnConfirm.isEnabled = enable
    }
}
