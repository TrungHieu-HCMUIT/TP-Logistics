package com.trunnghieu.tplogisticsapplication.binding

import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("showUnderline")
fun showUnderline(textView: TextView, showUnderline: Boolean) {
    if (showUnderline) {
        val content = SpannableString(textView.text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        textView.text = content
    }
}

@BindingAdapter("showItalicBoldDefBold")
fun showItalicBoldWithDefaultBold(textView: TextView, showItalicBold: Boolean) {
    if (showItalicBold) {
        textView.setTypeface(null, Typeface.BOLD_ITALIC)
        return
    }
    textView.setTypeface(null, Typeface.BOLD)
}

@BindingAdapter("htmlText")
fun setHtmlText(textView: TextView, htmlText: String) {
    textView.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

@BindingAdapter("passwordToggleEnabled")
fun setPasswordToggleEnabled(textInputLayout: TextInputLayout, passwordToggleEnabled: Boolean) {
    if (passwordToggleEnabled)
        textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    else
        textInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
}