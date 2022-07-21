package com.example.quizacademytask

import android.os.Bundle
import android.view.MenuItem
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
        AppDatabase.getInstance(this@MainActivity)

        if (savedInstanceState == null) {
            if (isTablet()) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return true
    }

    fun isTablet(): Boolean = resources.getBoolean(R.bool.isTablet)

}