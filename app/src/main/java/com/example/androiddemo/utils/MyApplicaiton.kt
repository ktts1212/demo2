package com.example.androiddemo.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplicaiton:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}