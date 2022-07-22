package com.example.quizacademytask

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.quizacademytask.databinding.FragmentFlashcardStackBinding
import db.AppDatabase
import db.entities.Card
import db.entities.CardStack
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class FlashcardStackFragment : Fragment() {

    private lateinit var pager: ViewPager2
    private lateinit var stack: CardStack
    private lateinit var db: AppDatabase
    private lateinit var cards: List<Card>
    private lateinit var toolbar: Toolbar
    private var NUM_PAGES = 0
    private lateinit var appContext: Context

    private var _binding: FragmentFlashcardStackBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // INITS
        appContext = requireContext()
        arguments?.let {
            stack = it.getSerializable("stack") as CardStack
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashcardStackBinding.inflate(inflater, container, false)

        runBlocking {
            launch {
                db = App.db
                cards = db.cardStackDAO().getCardStackAndCards(stack.cardStackId)[0].cards
            }
        }

        toolbar = binding.toolbarFlashcardStack
        requireActivity().setActionBar(toolbar)
        pager = binding.pager
        NUM_PAGES = stack.num_cards
        binding.toolbarFlashcardStack.title = stack.name

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })

        // ADAPTER
        val pagerAdapter = SlidePagerAdapter(this)
        pager.adapter = pagerAdapter

        return binding.root
    }

    private inner class SlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
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