package com.example.quizacademytask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.quizacademytask.databinding.FragmentStackListBinding
import db.dao.CardDAO
import db.dao.CardStackDAO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.io.IOException


private val courseId: Long = 28
private lateinit var swipeContainer: SwipeRefreshLayout
private lateinit var stacksList: ArrayList<String>
private lateinit var recyclerView: RecyclerView
private lateinit var idlingResource: CountingIdlingResource
private lateinit var cardStackDAO: CardStackDAO
private lateinit var cardDAO: CardDAO
private lateinit var stacksAdapter: SimpleAdapter
private lateinit var binding: FragmentStackListBinding
private lateinit var model: MainViewModel

var isTablet: Boolean = false

class StackListFragment : Fragment(), SimpleAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStackListBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        //INITS
        stacksList = model.stacksList
        recyclerView = binding.recyclerView
        isTablet = resources.getBoolean(R.bool.isTablet)
        initArrayAdapters()
        initSwipeDeleteFunction()
        recyclerView.adapter = stacksAdapter
        swipeContainer = binding.swipeContainer
        idlingResource = CountingIdlingResource("API")
        binding.toolbarStackList.title = "Topics"

        //DATABASE
        cardStackDAO = App.db.cardStackDAO()
        cardDAO = App.db.cardDAO()

        /*Check if course already exists in database. If not then download and insert*/
        runBlocking {
            launch {
                if (model.cardStacks.isEmpty())
                    getRequest()
                else
                    model.refillStacksList()
            }
        }

        //SWIPE REFRESH SETTINGS; Updates from API
        swipeContainer.setOnRefreshListener {
            getRequest()
            swipeContainer.isRefreshing = false
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initSwipeDeleteFunction() {
        //SWIPE DELETE SETTINGS
        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        stacksAdapter.deleteItem(viewHolder.adapterPosition)
                    }
                    ItemTouchHelper.RIGHT -> {
                        stacksAdapter.deleteItem(viewHolder.adapterPosition)
                    }
                }
            }

        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    /* Initialize the array adapters */
    private fun initArrayAdapters() {
        stacksAdapter = SimpleAdapter(stacksList, this)
    }


    /* Send GET request to API*/
    private fun getRequest() {
        EspressoIdlingResource.increment()
        val url = "https://api.quizacademy.io/quiz-dev/public/courses/$courseId"
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .header("x-api-key", App.apiKey)
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity!!.runOnUiThread {
                    Toast.makeText(
                        activity,
                        "Error downloading course",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                Log.e("JSON", e.toString())
                e.printStackTrace()
                EspressoIdlingResource.decrement()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unsuccessful response")
                    model.courseJSON = response.body?.string()!!
                    activity!!.runOnUiThread {
                        Toast.makeText(
                            activity,
                            "Course downloaded",
                            Toast.LENGTH_SHORT,
                        )
                            .show()
                        model.createStacks(model.courseJSON)
                        model.refillStacksList()
                        initSwipeDeleteFunction()
                    }
                }
                EspressoIdlingResource.decrement()
            }
        })
    }


    override fun onItemClick(position: Int, v: View?) {
        val item = stacksList[position]
        val stack = model.stackMap[item]
        val bundle = Bundle()
        bundle.putSerializable("stack", stack)
        val fragment = FlashcardStackFragment()
        fragment.arguments = bundle
        val ft = requireActivity().supportFragmentManager.beginTransaction()
        if (!isTablet) ft.replace(R.id.fragmentContainer, fragment)
        else ft.replace(R.id.fragmentContainer2, fragment)
        ft.addToBackStack(null)
            .commit()
    }

}
