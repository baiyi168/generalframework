package com.tll.generalframework

import android.app.Application

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: BaseApplication
    }
}