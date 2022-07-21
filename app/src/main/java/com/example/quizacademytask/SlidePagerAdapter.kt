package com.example.quizacademytask

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import db.entities.Card
import db.entities.CardStack

class SlidePagerAdapter(
    fa: Fragment,
    val NUM_PAGES: Int,
    val stack: CardStack,
    val cards: List<Card>
) : FragmentStateAdapter(fa) {

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
