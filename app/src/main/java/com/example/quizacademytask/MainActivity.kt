package com.example.quizacademytask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizacademytask.databinding.ActivityMainBinding
import db.AppDatabase

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initiate/Get database instance
        AppDatabase.getInstance(this)

        if (savedInstanceState == null) {
            if (this.isTablet()) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, StackListFragment())
                    .replace(R.id.fragmentContainer2, FlashcardStackFragment()).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, StackListFragment())
                    .commit()
            }
        }
    }
}
