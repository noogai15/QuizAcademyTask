package com.example.quizacademytask

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.quizacademytask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, StackListFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onBackPressed()
        return true
    }
}