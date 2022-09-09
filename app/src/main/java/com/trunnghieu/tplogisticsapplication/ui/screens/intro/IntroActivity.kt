package com.trunnghieu.tplogisticsapplication.ui.screens.intro

import android.app.Dialog
import android.util.Log
import androidx.core.animation.doOnEnd
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.ActivityIntroBinding
import com.trunnghieu.tplogisticsapplication.ui.base.activity.BaseActivity

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroVM>(), IntroUV {
    override fun layoutRes() = R.layout.activity_intro

    override fun viewModelClass(): Class<IntroVM> {
        return IntroVM::class.java
    }

    override fun initViewModel(viewModel: IntroVM) {
        viewModel.run {
            init(this@IntroActivity)
            initLifeCycle(this@IntroActivity)
        }
    }

    private var errorDialog: Dialog? = null

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initAction() {

    }

    override fun checkPlayServices() {
//        if (checkGooglePlayServices()) {
            startIntro()
//        }
    }

    override fun stopIntro() {
        binding.animationView.removeAllUpdateListeners()
    }

    private fun startIntro() {
        binding.animationView.run {
            playAnimation()
            addAnimatorUpdateListener { animator ->
                stopIntro()
                animator.doOnEnd {
//                    navigateTo(LoginActivity::class.java, true)
                    Log.d("DebugMode", "done animation")
                }
            }
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode: Int = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                if (errorDialog == null) {
                    errorDialog = googleApiAvailability.getErrorDialog(this, resultCode, 2404)
                    errorDialog?.setCancelable(false)
                }
                if (errorDialog?.isShowing == false) errorDialog?.show()
            }
        }
        return resultCode == ConnectionResult.SUCCESS
    }
}