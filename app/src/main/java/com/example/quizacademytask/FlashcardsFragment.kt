package com.example.quizacademytask

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FlashcardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FlashcardsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listView: ListView
    private lateinit var course: Course
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flashcards, container, false)

        //INITS
        listView = view.findViewById(R.id.flashcards_listview)
        val arrayList: ArrayList<String> = ArrayList<String>()

        //LIST VIEW
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(view.context, android.R.layout.simple_list_item_1, arrayList)
        listView.adapter = adapter

        course = createCourse(readJSON(file))
        for (stack in course.card_stacks) {
            arrayList.add(stack.name)
        }
        return view
    }

    @Throws(JsonSyntaxException::class)
    fun readJSON(file: File): String {
        val bReader: BufferedReader = BufferedReader(FileReader(file))
        val content = bReader.readLines()
        val s: StringBuilder = StringBuilder()
        for (line in content)
            if (!line.isEmpty()) s.append(line)
        bReader.close()
        return s.toString()
    }

    fun createCourse(jsonString: String): Course {
        val course = Gson().fromJson(jsonString, Course::class.java);
        return course
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FlashcardsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(file_path: String, file_name: String) =
            FlashcardsFragment().apply {
                arguments = Bundle().apply {
                    putString("file_path", file_path)
                    putString("file_name", file_name)
                }
            }
    }


}