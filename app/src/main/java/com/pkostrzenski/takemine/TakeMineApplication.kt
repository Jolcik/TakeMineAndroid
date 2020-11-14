package com.pkostrzenski.takemine

import android.app.Application
import android.content.Context

class TakeMineApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object{
        private lateinit var appContext: Context

        fun getAppContext(): Context {
            return appContext
        }
    }

}