package db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import db.dao.CardDAO
import db.dao.CardStackDAO
import db.entities.Card
import db.entities.CardStack

@Database(entities = [CardStack::class, Card::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardStackDAO(): CardStackDAO
    abstract fun cardDAO(): CardDAO

    companion object {
        const val DB_NAME = "flashcards_db"
        private var instance: AppDatabase? = null
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //CardStack Migration
                database.execSQL("BEGIN TRANSACTION")
                database.execSQL("ALTER TABLE CardStack RENAME TO CardStackOLD")
                database.execSQL(
                    "CREATE TABLE CardStack(" +
                            "cardStackId INTEGER NOT NULL PRIMARY KEY," +
                            "name TEXT NOT NULL," +
                            "num_cards INTEGER NOT NULL) "
                )
                database.execSQL("INSERT INTO CardStack(cardStackId, name, num_cards) SELECT cardStackId, name, num_cards FROM CardStackOLD")
                database.execSQL("COMMIT")

                //Card Migration
                database.execSQL("BEGIN TRANSACTION")
                database.execSQL("ALTER TABLE Card RENAME TO CardOLD")
                database.execSQL(
                    "CREATE TABLE Card(" +
                            "cardId INTEGER NOT NULL PRIMARY KEY," +
                            "cardStackId INTEGER NOT NULL," +
                            "answer TEXT NOT NULL," +
                            "explanation TEXT NOT NULL," +
                            "text TEXT NOT NULL," +
                            "FOREIGN KEY (cardStackId) REFERENCES CardStack(cardStackId) ON UPDATE CASCADE ON DELETE CASCADE) "
                )
                database.execSQL("INSERT INTO Card(cardId, cardStackId, answer, explanation, text) SELECT cardId, cardStackId, answer, explanation, text FROM CardOLD")
                database.execSQL("COMMIT")

                //Delete old tables
                database.execSQL("DROP TABLE Course")
                database.execSQL("DROP TABLE CardStackOLD")
                database.execSQL("DROP TABLE CardOLD")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDatabase
        }

        fun destroyInstance() {
            instance = null
        }

    }
}
