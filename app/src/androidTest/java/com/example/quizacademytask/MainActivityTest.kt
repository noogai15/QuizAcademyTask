package com.example.quizacademytask

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class MainActivityTest {

    @Test
    fun isNetworkAvailable() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        assert(activeNetworkInfo != null && activeNetworkInfo.isConnected)
    }

    @Test
    fun apiKeyExists() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val ai: ApplicationInfo = context.packageManager.getApplicationInfo(
            context.packageName, PackageManager.GET_META_DATA
        )
        val apiKey: String = ai.metaData["apiKey"] as String
        assert(apiKey != null)
    }

    @Test
    fun apiKeyWorks() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val ai: ApplicationInfo = context.packageManager.getApplicationInfo(
            context.packageName, PackageManager.GET_META_DATA
        )
        val apiKey: String = ai.metaData["apiKey"] as String
        val client = OkHttpClient();
        val request = Request.Builder()
            .url("https://api.quizacademy.io/quiz-dev/public/courses/28")
            .addHeader("x-api-key", apiKey)
            .build();
        val response = client.newCall(request).execute()
        assert(response.isSuccessful)
    }


}


