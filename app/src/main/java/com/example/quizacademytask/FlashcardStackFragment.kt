package com.example.quizacademytask

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
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

    private lateinit var _binding: FragmentFlashcardStackBinding
    private val binding get() = _binding

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
        handleTablet()

        runBlocking {
            launch {
                db = AppDatabase.getInstance(appContext)
                cards = db.cardStackDAO().getCardStackAndCards(stack!!.cardStackId)[0].cards
            }
        }

        toolbar = binding.toolbarFlashcardStack
        requireActivity().setActionBar(toolbar)
        pager = binding.pager
        NUM_PAGES = stack?.let { it.num_cards } ?: 0
        binding.toolbarFlashcardStack.title = stack!!.name

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // ADAPTER
        val pagerAdapter = SlidePagerAdapter(this, NUM_PAGES, stack, cards)
        pager.adapter = pagerAdapter

        return binding.root
    }


    private fun handleTablet() {
        if (resources.getBoolean(R.bool.isTablet)) {
            binding.toolbarFlashcardStack.navigationIcon = null
        }
    }
}