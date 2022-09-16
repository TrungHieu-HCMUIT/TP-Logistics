package com.trunnghieu.tplogisticsapplication.ui.screens.reset_password

import androidx.core.content.ContextCompat
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

    override fun resetValidate() {
        viewModel.errorMessage.value = ""
        binding.apply {
            val normalStrokeColor =
                ContextCompat.getColorStateList(context, R.color.til_stroke_color)!!
            inputPhone.textInputLayout.setBoxStrokeColorStateList(normalStrokeColor)
        }
    }

    override fun phoneNumberIsEmpty() {
        viewModel.errorMessage.value =
            context.getString(R.string.reset_pass_err_phone_number_is_empty)
        binding.apply {
            val errorStrokeColor =
                ContextCompat.getColorStateList(context, R.color.til_error_stroke_color)!!
            inputPhone.textInputLayout.setBoxStrokeColorStateList(errorStrokeColor)
            inputPhone.edittext.requestFocus()
        }
    }
}