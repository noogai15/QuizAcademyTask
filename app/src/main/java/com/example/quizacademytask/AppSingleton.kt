package com.example.quizacademytask

import android.app.Application
import db.AppDatabase

class AppSingleton : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = AppDatabase.getInstance(this)
    }

    companion object {
        lateinit var instance: AppSingleton
        lateinit var db: AppDatabase
    }
}