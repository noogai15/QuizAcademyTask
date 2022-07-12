package com.example.quizacademytask

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class EspressoTests {
    @RunWith(AndroidJUnit4::class)
    @LargeTest
    class HelloWorldEspressoTest {

        @get:Rule
        val activityRule = ActivityScenarioRule(MainActivity::class.java)

        @Test
        fun downloadsCourse() {
        }

        @Test
        fun clicksButton() {
            val view = Espresso.onView(ViewMatchers.withText("BWL Grundlagen"))
            view.perform(ViewActions.click())
        }
    }
}