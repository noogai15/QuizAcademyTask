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
    private var stack: CardStack? = null
    private lateinit var db: AppDatabase
    private lateinit var cards: List<Card>
    private lateinit var toolbar: Toolbar
    private var numPages = 0
    private lateinit var appContext: Context
    private lateinit var binding: FragmentFlashcardStackBinding
    private lateinit var main: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = requireContext()
        main = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlashcardStackBinding.inflate(inflater, container, false)
        arguments?.let {
            stack = it.getSerializable("stack") as CardStack
        }
        if (main.isTablet()) disableNavBack()

        //If no stack has been clicked yet, leave fragment empty
        if (stack == null) return binding.root

        runBlocking {
            launch {
                db = AppDatabase.getInstance(appContext)
                cards = db.cardStackDAO().getCardStackAndCards(stack!!.cardStackId)[0].cards
            }
        }

        //INITS
        toolbar = binding.toolbarFlashcardStack
        requireActivity().setActionBar(toolbar)
        pager = binding.pager
        numPages = stack?.let { it.num_cards } ?: 0
        binding.toolbarFlashcardStack.title = stack!!.name
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // ADAPTER
        val pagerAdapter = SlidePagerAdapter(this, numPages, stack!!, cards)
        pager.adapter = pagerAdapter

        return binding.root
    }

    private fun disableNavBack() {
        binding.toolbarFlashcardStack.navigationIcon = null
    }

}