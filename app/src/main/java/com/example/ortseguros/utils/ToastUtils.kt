package com.example.ortseguros.utils

import android.content.Context
import android.widget.Toast

class ToastUtils {
    companion object {
        fun mostrarToast(context: Context?, mensaje: String) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }
    }
}