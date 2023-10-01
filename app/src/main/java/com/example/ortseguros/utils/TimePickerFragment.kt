package com.example.ortseguros.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.TimeZone

class TimePickerFragment(val listener: (String) -> Unit) : DialogFragment(),TimePickerDialog.OnTimeSetListener{


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: java.util.Calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"))
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minut: Int = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(activity as Context, this, hour, minut, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
       listener("$hourOfDay:$minute" )



    }

}