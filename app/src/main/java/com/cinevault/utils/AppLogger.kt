package com.cinevault.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object AppLogger {

    fun putDebugLog(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun putErrorLog(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun putWarningLog(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun putInfoLog(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}