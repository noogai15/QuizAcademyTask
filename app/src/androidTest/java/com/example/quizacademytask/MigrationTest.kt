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

        val stackCursor = db.query("SELECT * FROM CardStack")
        val cardCursor = db.query("SELECT * FROM Card")
        stackCursor.moveToFirst()
        cardCursor.moveToFirst()

        val stackId = stackCursor.getInt(0)
        val stackName = stackCursor.getString(1)
        val stackNumCards = stackCursor.getInt(2)
        val cardId = cardCursor.getInt(0)
        val cardFK = cardCursor.getInt(1)
        val cardAnswer = cardCursor.getString(2)
        val cardExplanation = cardCursor.getString(3)
        val cardText = cardCursor.getString(4)

        assertEquals(0, stackId)
        assertEquals("testStack", stackName)
        assertEquals(4, stackNumCards)
        assertEquals(0, cardId)
        assertEquals(0, cardFK)
        assertEquals("testAnswer", cardAnswer)
        assertEquals("testExplanation", cardExplanation)
        assertEquals("testText", cardText)

        db.close()
    }


}
