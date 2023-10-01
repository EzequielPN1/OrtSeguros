package com.example.ortseguros.fragments.home.siniestros

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NuevoSiniestroViewModel : ViewModel() {


    val selectedDateLiveData = MutableLiveData<String>()
    fun onDateSelected(day: Int, month: Int, year: Int) {
        val fechaDeSiniestro = "$day/${month + 1}/$year"
        selectedDateLiveData.value = fechaDeSiniestro
    }



    val selectedTimeLiveData = MutableLiveData<String>()
     fun onTimeSelected(time:String){
        val horaSiniestro = Editable.Factory.getInstance().newEditable(time)
         selectedTimeLiveData.value = horaSiniestro.toString()
    }



}