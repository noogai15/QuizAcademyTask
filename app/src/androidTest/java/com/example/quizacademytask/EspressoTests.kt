package com.example.quizacademytask

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest

class EspressoTests {

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun openCourseTest() {
        val stack = onView(withText("Jura"))
        stack.perform(click())
        val viewPager = onView(withId(R.id.pager))
        val flashCardLayout = onView(withId(R.id.flashcardLayout))
        flashCardLayout.perform(swipeLeft())
        flashCardLayout.perform(click())
        flashCardLayout.perform(swipeRight())
        flashCardLayout.perform(click())
        flashCardLayout.perform(pressBack())

    }

    @Test
    //List refresh test
    fun listTest() {
        onView(withId(R.id.swipeContainer)).perform(swipeDown())
    }

    @Test
    fun deleteStackTest() {
        val stack = onView(withText("Jura"))
        stack.perform(swipeLeft())
    }
}
