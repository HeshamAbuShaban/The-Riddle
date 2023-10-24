package dev.training.the_riddle.app_system

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppInstance private constructor() : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        @Volatile
        var INSTANCE: AppInstance? = null

        @JvmStatic
        fun getInstance(): AppInstance = INSTANCE ?: synchronized(this) {
            AppInstance().also { INSTANCE = it }
        }
    }

}