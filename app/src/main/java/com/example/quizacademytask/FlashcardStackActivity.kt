package com.example.quizacademytask

import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.quizacademytask.databinding.ActivityFlashcardStackBinding

class FlashcardStackActivity : FragmentActivity() {

    private lateinit var pager: ViewPager2
    private lateinit var stack: CardStack
    private lateinit var toolbar: Toolbar
    private var NUM_PAGES = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFlashcardStackBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // INITS
        stack = intent.getSerializableExtra("stack") as CardStack
        pager = binding.pager
        NUM_PAGES = stack.num_cards
        toolbar = binding.toolbar
        setActionBar(toolbar)
        toolbar.title = stack.name

        // ADAPTER
        val pagerAdapter = SlidePagerAdapter(this)
        pager.adapter = pagerAdapter


    }

    override fun onBackPressed() {
        if (pager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            pager.currentItem = pager.currentItem - 1
        }
    }


    private inner class SlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment {
            val frag = CardFragment()
            frag.apply {
                arguments = Bundle().apply {
                    putString("TOPIC", stack.name)
                    putString("QUESTION", stack.cards[position].text)
                    putString("ANSWER", stack.cards[position].explanation)
                }
            }
            return frag
        }

    }
}