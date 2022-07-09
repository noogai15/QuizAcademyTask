package com.example.quizacademytask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CardFragment : Fragment() {
    private var topic: String? = null
    private var question: String? = null
    private var answer: String? = null
    private var peeking: Boolean = false


    //TODO: Use Question class etc here?
    private lateinit var topicTV: TextView
    private lateinit var textTV: TextView
    private lateinit var cardType: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            topic = it.getString("TOPIC")
            question = it.getString("QUESTION")
            answer = it.getString("ANSWER")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        //INITS
        val view: View = inflater.inflate(R.layout.fragment_card, container, false)
        topicTV = view.findViewById(R.id.topicTV)
        textTV = view.findViewById(R.id.textTV)
        cardType = view.findViewById(R.id.card_type)

        topicTV.text = topic
        textTV.text = question
        cardType.text = "Question"

        view.setOnClickListener {
            if (!peeking) {
                peeking = true
                cardType.text = "Answer"
                textTV.text = answer
            } else {
                peeking = false
                cardType.text = "Question"
                textTV.text = question
            }

        }
        return view
    }
}