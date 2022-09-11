package com.trunnghieu.tplogisticsapplication.extensions

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Go to activity
 */
fun <T> Activity.navigateTo(
    activityClass: Class<T>,
    alsoFinishCurrentActivity: Boolean? = false
) {
    startActivity(Intent(this, activityClass))
    if (alsoFinishCurrentActivity == true) {
        finish()
    }
}

/**
 * Go to activity
 */
fun <T> Fragment.navigateTo(
    activityClass: Class<T>,
    alsoFinishCurrentActivity: Boolean? = false
) {
    startActivity(Intent(activity, activityClass))
    if (alsoFinishCurrentActivity == true) {
        activity?.finish()
    }
}