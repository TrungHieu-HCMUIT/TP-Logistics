package com.trunnghieu.tplogisticsapplication.ui.screens.login.language

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.trunnghieu.tplogisticsapplication.databinding.PopupMenuLanguageBinding
import com.trunnghieu.tplogisticsapplication.ui.base.adapter.BaseItemClickListener

class LanguagePopupMenu(private val context: Context, private val anchorView: View) {

    private var popupWindow: PopupWindow? = null

    @SuppressLint("ClickableViewAccessibility")
    fun show(
        currentLanguage: String,
        popupDismissCallback: () -> Unit,
        languageSelectedCallback: (LanguageMenu) -> Unit
    ) {
        val popupBinding = PopupMenuLanguageBinding.inflate(
            LayoutInflater.from(context), null, true
        )

        popupWindow = PopupWindow(
            popupBinding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupBinding.apply {
            this.currentLanguage = currentLanguage
            val languageMenuAdapter = LanguageMenuAdapter(
                context,
                object : BaseItemClickListener<LanguageMenu> {
                    override fun onItemClick(item: LanguageMenu) {
                        dismiss()
                        languageSelectedCallback(item)
                    }
                }
            )
            rvLanguage.adapter = languageMenuAdapter
            languageMenuAdapter.setSelectedItem(currentLanguage)
        }

        popupWindow!!.apply {
            showAsDropDown(anchorView)
            setOnDismissListener { popupDismissCallback() }
        }
    }

    private fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
    }

}