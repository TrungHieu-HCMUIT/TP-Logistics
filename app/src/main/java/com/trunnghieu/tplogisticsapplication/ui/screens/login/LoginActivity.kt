package com.trunnghieu.tplogisticsapplication.ui.screens.login

import android.text.method.PasswordTransformationMethod
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.ActivityLoginBinding
import com.trunnghieu.tplogisticsapplication.extensions.navigateTo
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity
import com.trunnghieu.tplogisticsapplication.ui.screens.reset_password.ResetPasswordActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginVM>(), LoginUV{

    override fun layoutRes() = R.layout.activity_login

    override fun viewModelClass(): Class<LoginVM> {
        return LoginVM::class.java
    }

    override fun initViewModel(viewModel: LoginVM) {
        viewModel.run {
            init(this@LoginActivity)
            initLifeCycle(this@LoginActivity)
            binding.vm = this
        }
    }

    override fun initView() {
        binding.inputPassword.edittext.transformationMethod = PasswordTransformationMethod()
    }

    override fun initData() {
        
    }

    override fun initAction() {
        
    }

    override fun goToResetPassword() {
        navigateTo(ResetPasswordActivity::class.java)
    }

    override fun showLanguagePopup() {
        
    }

}