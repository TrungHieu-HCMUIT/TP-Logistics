package com.trunnghieu.tplogisticsapplication.ui.screens.phone_verify

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.FragmentPhoneVerifyBinding
import com.trunnghieu.tplogisticsapplication.ui.base.dialog.AppAlertDialog
import com.trunnghieu.tplogisticsapplication.ui.base.fragment.BaseFragment
import com.trunnghieu.tplogisticsapplication.ui.screens.job.JobVM

class PhoneVerifyFragment : BaseFragment<FragmentPhoneVerifyBinding, PhoneVerifyVM>(),
    PhoneVerifyUV {

    companion object {
        const val ARG_PHONE_NUMBER = "ARG_PHONE_NUMBER"
        const val ARG_EXPIRES_TIME = "ARG_EXPIRES_TIME"

        fun newInstance(newPhoneNumber: String, expiresTime: Int) = PhoneVerifyFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PHONE_NUMBER, newPhoneNumber)
                putInt(ARG_EXPIRES_TIME, expiresTime)
            }
        }
    }

    override fun layoutRes() = R.layout.fragment_phone_verify

    override fun viewModelClass(): Class<PhoneVerifyVM> {
        return PhoneVerifyVM::class.java
    }

    private val jobVM by activityViewModels<JobVM>()

    override fun initViewModel(viewModel: PhoneVerifyVM) {
        viewModel.run {
            init(this@PhoneVerifyFragment)
            initLifecycle(this@PhoneVerifyFragment)
            binding.vm = this
        }
        binding.jobVM = jobVM
    }

    private var logoutDialog: AlertDialog? = null

    override fun initView() {

    }

    override fun initData() {
        binding.viewVerifyCode.getVerifyCode().observe(this) { verifyCode ->
            viewModel.otpCode.value = verifyCode
        }

        viewModel.getExtras(arguments)
    }

    override fun initAction() {
    }

    override fun clearOtpCode() {
        binding.viewVerifyCode.clearText()
    }

    override fun updatePhoneNumberSuccess() {
        showAlert(
            getString(R.string.otp_update_phone_success_msg),
            onClickListener = object : AppAlertDialog.AlertDialogOnClickListener {
                override fun onPositiveClick() {
                    logoutDialog?.dismiss()
                    // Logout user
                    jobVM.logOut()
                }
            }
        )
    }

}