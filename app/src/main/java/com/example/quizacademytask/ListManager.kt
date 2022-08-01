package com.example.quizacademytask

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView

class ListManager(var selectedItems: HashMap<Int, View>) {

    fun process(view: TextView, position: Int) {
        if (!selectedItems.containsKey(position)) {
            selectedItems[position] = view
            paintSelected(view)
        } else {
            selectedItems.remove(position)
            paintUnselected(view)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun paintSelected(view: View) {
        view.findViewById<TextView>(R.id.rowText).setTextColor(0xFFFF0000.toInt())
    }

    fun emptyList() {
        for (view in selectedItems.values) paintUnselected(view)
        selectedItems.clear()
    }

    @SuppressLint("ResourceAsColor")
    fun paintUnselected(view: View) {
        //paint text white
        view.findViewById<TextView>(R.id.rowText).setTextColor(0xFFFDFDFD.toInt())
    }
}
