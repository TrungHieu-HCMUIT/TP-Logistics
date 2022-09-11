package com.trunnghieu.tplogisticsapplication.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class MovableFloatingActionButton : FloatingActionButton, View.OnTouchListener {

    private var customClickListener: CustomClickListener? = null
    private var downRawX = 0f
    private var downRawY = 0f
    private var dX = 0f
    private var dY = 0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        val action = motionEvent.action

        return if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.rawX
            downRawY = motionEvent.rawY
            dX = view.x - downRawX
            dY = view.y - downRawY
            true // Consumed
        } else if (action == MotionEvent.ACTION_MOVE) {
            val viewWidth: Int = view.width
            val viewHeight: Int = view.height
            val viewParent: View = view.parent as View
            val parentWidth: Int = viewParent.width
            val parentHeight: Int = viewParent.height
            var newX = motionEvent.rawX + dX
            newX = max(
                layoutParams.leftMargin.toFloat(),
                newX
            ) // Don't allow the FAB past the left hand side of the parent
            newX = min(
                (parentWidth - viewWidth - layoutParams.rightMargin).toFloat(),
                newX
            ) // Don't allow the FAB past the right hand side of the parent
            var newY = motionEvent.rawY + dY
            newY = max(
                layoutParams.topMargin.toFloat(),
                newY
            ) // Don't allow the FAB past the top of the parent
            newY = min(
                (parentHeight - viewHeight - layoutParams.bottomMargin).toFloat(),
                newY
            ) // Don't allow the FAB past the bottom of the parent
            view.animate()
                .x(newX)
                .y(newY)
                .setDuration(0)
                .start()
            true // Consumed
        } else if (action == MotionEvent.ACTION_UP) {
            val upRawX = motionEvent.rawX
            val upRawY = motionEvent.rawY
            val upDX = upRawX - downRawX
            val upDY = upRawY - downRawY
            if (abs(upDX) < CLICK_DRAG_TOLERANCE && abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                customClickListener?.onClick(view)
                true
            } else { // A drag
                true // Consumed
            }
        } else {
            super.onTouchEvent(motionEvent)
        }
    }

    fun setCustomClickListener(customClickListener: CustomClickListener) {
        this.customClickListener = customClickListener
    }

    interface CustomClickListener {
        fun onClick(view: View?)
    }

    companion object {
        private const val CLICK_DRAG_TOLERANCE =
            10f // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    }
}