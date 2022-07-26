package com.example.quizacademytask

import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import db.AppDatabase
import junit.framework.Assert.*
import org.junit.Rule
import org.junit.Test

class MigrationTest {
    private val testDBName = "test_db"

    @JvmField
    @Rule
    val testHelper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::
        class.java.canonicalName, FrameworkSQLiteOpenHelperFactory()
    )

    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE IF EXISTS course;")
            database.execSQL("ALTER TABLE `cardStack` RENAME TO `cardStackOLD`")
            database.execSQL(
                "CREATE TABLE `cardStack`(" +
                        "`cardStackId` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL," +
                        "`num_cards` INTEGER NOT NULL) "
            )
            database.execSQL("DROP TABLE `cardStackOLD`")
        }
    }

    @Test
    fun migrate1to2() {
        val db = testHelper.createDatabase(testDBName, 1).apply {
            execSQL(
                """
            INSERT INTO Course (courseId, name, num_cards, num_stacks) VALUES (0, "testCourse", 4, 4);
            INSERT INTO CardStack (cardStackId, courseId, name, num_cards) VALUES (0, 0, 'testStack', 4);
            INSERT INTO Card (cardId, answer, explanation, text) VALUES (0, "testAnswer", 'testExplanation', "testText");
            """.trimIndent()
            )
        }
        testHelper.runMigrationsAndValidate(testDBName, 2, true, MIGRATION_1_2)
        db.close()

        val resultCursor = db.query("SELECT * FROM CardStack")
        assertTrue(resultCursor.moveToFirst())

        assertEquals(0, resultCursor.getInt(resultCursor.getColumnIndex("cardStackId")))
        assertNull(resultCursor.getInt(resultCursor.getColumnIndex("courseId")))
    }


}
