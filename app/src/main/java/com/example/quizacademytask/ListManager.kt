package com.example.quizacademytask

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView

class ListManager(var selectedItems: ArrayList<TextView>) {

    fun process(view: TextView) {
        if (!selectedItems.contains(view)) {
            selectedItems.add(view)
            paintSelected(view)
        } else {
            selectedItems.remove(view)
            paintUnselected(view)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun paintSelected(view: View) {
        view.findViewById<TextView>(R.id.rowText).setTextColor(0xFFFF0000.toInt())
    }

    fun emptyList() {
        for (view in selectedItems) paintUnselected(view)
        selectedItems.clear()
    }

    @SuppressLint("ResourceAsColor")
    fun paintUnselected(view: TextView) {
        view.setTextColor(0xFFFDFDFD.toInt())
    }
}
