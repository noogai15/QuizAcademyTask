package com.example.quizacademytask

import android.app.Application
import android.content.Context
import db.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
        context = applicationContext
    }

    companion object {
        lateinit var db: AppDatabase
        lateinit var context: Context
    }
}