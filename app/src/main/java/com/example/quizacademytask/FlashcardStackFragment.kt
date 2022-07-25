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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding = FragmentFlashcardStackBinding.inflate(inflater, container, false)

        //Set the toolbar
        toolbar = binding.toolbarFlashcardStack
        requireActivity().setActionBar(toolbar)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        if (appContext.isTablet()) {
            toolbar.disableNavBack()
        }

        //If no stack has been clicked yet, leave the rest of the fragment empty
        if (stack == null) return binding.root

        //INITS
        pager = binding.pager
        numPages = stack?.let { it.num_cards } ?: 0
        toolbar.title = stack!!.name


        runBlocking {
            launch {
                cards = App.db.cardStackDAO().getCardStackAndCards(stack!!.cardStackId)[0].cards
                db = AppDatabase.getInstance(appContext)
            }
        }


        // ADAPTER
        val pagerAdapter = SlidePagerAdapter(this, numPages, stack!!, cards)
        pager.adapter = pagerAdapter

        return binding.root
    }


}



