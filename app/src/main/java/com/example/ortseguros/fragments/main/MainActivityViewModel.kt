package com.example.ortseguros.fragments.main

import android.content.Context
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    fun isLoggedIn(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean("isLoggedIn", false)
    }

}