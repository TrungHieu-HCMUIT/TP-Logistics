package com.trunnghieu.tplogisticsapplication.ui.base.dialog

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

object DateTimePicker {

    /**
     * Create material date picker
     */
    fun createDatePicker(
        limitToCurrentDate: Boolean? = false,
        limitToStart: Boolean? = true,
        selection: Long = 0,
        theme: Int? = -1
    ): MaterialDatePicker<Long> {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                if (limitToCurrentDate == true) {
                    limitToCurrentDate(limitToStart ?: true).build()
                } else {
                    CalendarConstraints.Builder()
                        .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                }
            )
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)

        // Set style
        if (theme != null || theme != -1) {
            datePicker.setTheme(theme!!)
        }

        // Pre selected date
        if (selection != 0L) {
            datePicker.setSelection(selection)
        }

        return datePicker.build()
    }

    private fun limitToCurrentDate(limitToStart: Boolean): CalendarConstraints.Builder {
        return CalendarConstraints.Builder().apply {
            val currentDate: Long = Calendar.getInstance().timeInMillis
            if (limitToStart) setStart(currentDate) else setEnd(currentDate)
            setValidator(RangeStartValidator(currentDate, limitToStart))
        }
    }

    /**
     * Create material time picker
     */
    fun createTimePicker(): MaterialTimePicker {
        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()
    }

    internal class RangeStartValidator : CalendarConstraints.DateValidator {

        private var currentDate: Long
        var limitToStart: Boolean = true

        constructor(currentDate: Long, limitToStart: Boolean) {
            this.currentDate = currentDate
            this.limitToStart = limitToStart
        }

        constructor(parcel: Parcel) {
            currentDate = parcel.readLong()
        }

        override fun isValid(date: Long): Boolean {
            return if (limitToStart) currentDate <= date else currentDate >= date
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeLong(currentDate)
        }

        companion object CREATOR : Parcelable.Creator<RangeStartValidator> {
            override fun createFromParcel(parcel: Parcel): RangeStartValidator {
                return RangeStartValidator(parcel)
            }

            override fun newArray(size: Int): Array<RangeStartValidator?> {
                return arrayOfNulls(size)
            }
        }
    }
}