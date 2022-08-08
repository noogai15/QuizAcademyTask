package com.example.quizacademytask

import android.util.Log
import okhttp3.*
import java.io.IOException

class BackendClient {

    /* Send GET request to API*/
    fun requestCourse(
        courseId: Int,
        responseCallback: (courseJSON: String) -> Unit,
        errorCallback: () -> Unit,
    ) {
        EspressoIdlingResource.increment()
        var courseJSON = ""
        val url =
            "https://api.quizacademy.io/quiz-dev/public/courses/${courseId}"
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .header("x-api-key", App.apiKey)
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback()
                Log.e("ERROR", e.toString())
                e.printStackTrace()
                EspressoIdlingResource.decrement()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unsuccessful response")
                    courseJSON = response.body!!.string()
                    responseCallback.invoke(courseJSON)
                }
                EspressoIdlingResource.decrement()
            }
        })
    }
}