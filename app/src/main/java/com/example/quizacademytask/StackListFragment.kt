package com.example.quizacademytask

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.quizacademytask.databinding.FragmentStackListBinding
import com.google.gson.Gson
import db.AppDatabase
import db.dao.CardDAO
import db.dao.CardStackDAO
import db.dao.CourseDAO
import db.entities.Card
import db.entities.CardStack
import db.entities.Course
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.io.IOException

private val courseId = 28
private lateinit var swipeContainer: SwipeRefreshLayout
private lateinit var recyclerView: RecyclerView
private lateinit var stacksAdapter: SimpleAdapter
private lateinit var stacksList: ArrayList<String>
private lateinit var stackMap: HashMap<String, CardStack> //Pairing stack names and the stacks
private lateinit var courseJSON: String
private lateinit var idlingResource: CountingIdlingResource
private lateinit var db: AppDatabase
private lateinit var courseDAO: CourseDAO
private lateinit var cardStackDAO: CardStackDAO
private lateinit var cardDAO: CardDAO
private lateinit var courseObj: CourseObject
private lateinit var appContext: Context

class StackListFragment : Fragment(), SimpleAdapter.OnItemClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentStackListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStackListBinding.inflate(inflater, container, false)

        //INITS
        appContext = requireActivity().applicationContext
        recyclerView = binding.recyclerView
        stacksList = ArrayList()
        initArrayAdapters()
        initSwipeDeleteFunction()
        recyclerView.adapter = stacksAdapter
        stackMap = HashMap()
        courseJSON = ""
        swipeContainer = binding.swipeContainer
        idlingResource = CountingIdlingResource("API")
        binding.toolbarStackList.title = "Topics"

        //DATABASE
        db = AppSingleton.db
        courseDAO = db.courseDao()
        cardStackDAO = db.cardStackDAO()
        cardDAO = db.cardDAO()

        /*Check if course already exists in database. If not then download and insert*/
        runBlocking {
            launch {
                if (!courseDAO.isExists(courseId))
                    getRequest()
                else
                    refillStacksList()
            }
        }

        //SWIPE REFRESH SETTINGS
        swipeContainer.setOnRefreshListener {
            refillStacksList()
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

    /* Create a Course from JSON */
    private fun createCourse(jsonString: String): Course {
        var course: Course? = null
        runBlocking {
            launch {
                courseObj = Gson().fromJson(jsonString, CourseObject::class.java)
                val courseRowId = dbEntries(courseObj)
                course = courseDAO.getById(courseRowId.toInt())
            }
        }
        return course!!
    }

    private suspend fun dbEntries(courseObj: CourseObject): Long {
        val courseRowId =
            courseDAO.insert(
                Course(
                    courseObj.id,
                    courseObj.name,
                    courseObj.num_cards,
                    courseObj.num_stacks
                )
            )
        for (stack in courseObj.card_stacks) {
            cardStackDAO.insert(
                CardStack(
                    stack.id,
                    courseObj.id,
                    stack.name,
                    stack.num_cards
                )
            )
            for (card in stack.cards) {
                cardDAO.insert(
                    Card(
                        card.id,
                        stack.id,
                        card.answer,
                        card.explanation,
                        card.text
                    )
                )
            }
        }
        return courseRowId
    }

    /* Send GET request to API*/
    private fun getRequest() {
        EspressoIdlingResource.increment()
        val url = "https://api.quizacademy.io/quiz-dev/public/courses/$courseId"
        val client = OkHttpClient()
        val ai: ApplicationInfo = appContext.packageManager.getApplicationInfo(
            appContext.packageName,
            PackageManager.GET_META_DATA
        )
        val value: String = ai.metaData["apiKey"] as String

        val request: Request = Request.Builder()
            .header("x-api-key", value)
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
                    courseJSON = response.body?.string()!!
                    activity!!.runOnUiThread {
                        Toast.makeText(
                            activity,
                            "Course downloaded",
                            Toast.LENGTH_SHORT,
                        )
                            .show()
                        createCourse(courseJSON)
                        refillStacksList()
                        initSwipeDeleteFunction()
                    }
                }
                EspressoIdlingResource.decrement()
            }
        })
    }

    /* Refill the card stacks list */
    fun refillStacksList() {
        stacksList.clear()
        runBlocking {
            launch {
                val cardStacks: List<CardStack> =
                    courseDAO.getCourseAndCardStacks(courseId)[0].cardStacks

                for (stack in cardStacks) {
                    stacksList.add(stack.name)
                    stackMap[stack.name] = stack
                }
                stacksAdapter.notifyDataSetChanged()
                stacksList.sortBy { it } //Alphabetical sort
            }
        }
    }

    override fun onItemClick(position: Int, v: View?) {
        val item = stacksList[position]
        val stack = stackMap[item]
        val bundle = Bundle()
        bundle.putSerializable("stack", stack)
        val fragment = FlashcardStackFragment()
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

}