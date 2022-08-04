package com.example.quizacademytask

import android.app.Application
import android.content.pm.PackageManager
import db.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
        apiKey = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        ).metaData["apiKey"] as String
    }

    companion object {
        lateinit var db: AppDatabase
        lateinit var apiKey: String
    }
}
