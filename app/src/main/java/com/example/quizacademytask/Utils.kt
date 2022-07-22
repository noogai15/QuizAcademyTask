package com.example.quizacademytask

import android.content.Context
import android.widget.Toolbar

fun Context.isTablet(): Boolean {
    return resources.getBoolean(R.bool.isTablet)
}
 fun Toolbar.disableNavBack() {
    navigationIcon = null
}
