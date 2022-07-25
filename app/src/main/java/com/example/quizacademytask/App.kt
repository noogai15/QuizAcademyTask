package com.example.quizacademytask

import android.app.Application
import db.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
    }

    companion object {
        lateinit var db: AppDatabase
    }
}