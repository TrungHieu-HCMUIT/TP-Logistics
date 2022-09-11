package com.trunnghieu.tplogisticsapplication.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener

fun ViewGroup.addViewObserver(function: (View) -> Unit) {
    val view = this
    view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            function.invoke(view)
        }
    })
}
