package com.trunnghieu.tplogisticsapplication.ui.screens.reset_password

import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.ActivityResetPasswordBinding
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding, ResetPasswordVM>(),
    ResetPasswordUV {

    override fun layoutRes() = R.layout.activity_reset_password

    override fun viewModelClass(): Class<ResetPasswordVM> {
        return ResetPasswordVM::class.java
    }

    override fun initViewModel(viewModel: ResetPasswordVM) {
        viewModel.run {
            init(this@ResetPasswordActivity)
            binding.vm = this
        }
    }

    override fun initView() {

    }

    override fun initData() {
    }

    override fun initAction() {

    }
}