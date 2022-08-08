package com.example.quizacademytask

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizacademytask.databinding.ActivityMainBinding
import db.AppDatabase

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    public lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        model = ViewModelProvider(this)[MainViewModel::class.java]
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return true
    }
}
