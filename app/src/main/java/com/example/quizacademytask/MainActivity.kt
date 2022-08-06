package com.example.quizacademytask

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.example.quizacademytask.databinding.ActivityMainBinding
import db.AppDatabase

class MainActivity : AppCompatActivity(), MenuProvider {

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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        TODO("Not yet implemented")
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
