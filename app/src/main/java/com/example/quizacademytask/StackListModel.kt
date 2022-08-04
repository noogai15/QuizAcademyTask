package com.example.quizacademytask

import androidx.lifecycle.ViewModel

class StackListModel : ViewModel() {
    private val stacksList by lazy { ArrayList<String>() }.also { }
    private val cardDAO by lazy { App.db.cardDAO() }
    private val cardStackDAO by lazy { App.db.cardStackDAO() }

}

