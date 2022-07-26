package com.example.quizacademytask

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import db.AppDatabase
import junit.framework.Assert.assertEquals
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

    @Test
    fun migrate1to2() {
        val db = testHelper.createDatabase(testDBName, 1).apply {
            execSQL("INSERT INTO Course (courseId, name, num_cards, num_stacks) VALUES (0, 'testCourse', 4, 4);")
            execSQL("INSERT INTO CardStack (cardStackId, courseId, name, num_cards) VALUES (0, 0, 'testStack', 4);")
            execSQL("INSERT INTO Card (cardId, cardStackId, answer, explanation, text) VALUES (0, 0, 'testAnswer' , 'testExplanation', 'testText');")
        }
        testHelper.runMigrationsAndValidate(testDBName, 2, true, AppDatabase.MIGRATION_1_2)

        val resultCursor = db.query("SELECT * FROM CardStack")
        resultCursor.moveToFirst()

        val id = resultCursor.getInt(0)
        val name = resultCursor.getString(1)
        val numCards = resultCursor.getInt(2)

        assertEquals(1, id)
        assertEquals("testStack", name)
        assertEquals(4, numCards)

        db.close()
    }


}
