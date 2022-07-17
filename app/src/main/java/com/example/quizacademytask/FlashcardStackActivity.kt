package com.example.quizacademytask

import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.quizacademytask.databinding.ActivityFlashcardStackBinding
import db.AppDatabase
import db.entities.Card
import db.entities.CardStack
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class FlashcardStackActivity : FragmentActivity() {

    private val binding: ActivityFlashcardStackBinding by lazy {
        ActivityFlashcardStackBinding.inflate(layoutInflater)
    }
    private lateinit var pager: ViewPager2
    private lateinit var stack: CardStack
    private lateinit var toolbar: Toolbar
    private lateinit var db: AppDatabase
    private lateinit var cards: List<Card>
    private var NUM_PAGES = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Get Database instance
        // INITS
        stack = intent.getSerializableExtra("stack") as CardStack
        runBlocking {
            launch {
                db = AppDatabase.getInstance(this@FlashcardStackActivity)
                cards = db.cardStackDAO().getCardStackAndCards(stack.cardStackId)[0].cards
            }
        }

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
                    putString("QUESTION", cards[position].text)
                    putString("ANSWER", cards[position].explanation)
                }
            }
            return frag
        }

    }
}