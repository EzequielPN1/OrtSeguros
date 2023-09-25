package com.example.ortseguros.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ortseguros.R
import com.google.firebase.FirebaseApp

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)
    }





}