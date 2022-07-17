package db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import db.dao.CardDAO
import db.dao.CardStackDAO
import db.dao.CourseDAO
import db.entities.Card
import db.entities.CardStack
import db.entities.Course

@Database(entities = [Course::class, CardStack::class, Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDAO
    abstract fun cardStackDAO(): CardStackDAO
    abstract fun cardDAO(): CardDAO

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()
                }
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).build()
            }
            return instance as AppDatabase
        }

        fun destroyInstance() {
            instance = null
        }
    }
}