package com.trunnghieu.tplogisticsapplication.ui.widgets

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.trunnghieu.tplogisticsapplication.R
import com.trunnghieu.tplogisticsapplication.databinding.WidgetVerifyCodeBinding

class VerifyCode(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        private const val TOTAL_CODE_REQUIRES = 6
    }

    init {
        init()
    }

    private var inputMethodManager = context.applicationContext
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

    // View
    private lateinit var binding: WidgetVerifyCodeBinding
    private var edtVerifyCode: EditText? = null
    private val verifyCode = MutableLiveData("")

    private fun init() {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.widget_verify_code,
            this,
            true
        )
        edtVerifyCode = binding.edtVerifyCode

        // Filter
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(TOTAL_CODE_REQUIRES)
        edtVerifyCode!!.filters = filterArray
        // Text Changes
        edtVerifyCode!!.addTextChangedListener { editable ->
            val code = editable?.toString()
            code?.let { string ->
                verifyCode.value = string
                string.toList()
                    .map { it.toString() }
                    .toTypedArray().also {
                        binding.codeList = it
                    }
            }
        }

        // Action
        binding.showKeyboardOnClick = OnClickListener { showKeyboard() }
        showKeyboard()
    }

    private fun showKeyboard() {
        edtVerifyCode!!.requestFocus()
        inputMethodManager?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    fun hideKeyboard() {
        inputMethodManager?.hideSoftInputFromWindow(
            edtVerifyCode!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun getVerifyCode(): MutableLiveData<String> {
        return verifyCode
    }

    fun clearText() {
        if (edtVerifyCode == null) return
        edtVerifyCode!!.setText("")
    }
}