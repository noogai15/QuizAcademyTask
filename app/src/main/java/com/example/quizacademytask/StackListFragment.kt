package com.example.quizacademytask

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.quizacademytask.databinding.FragmentStackListBinding
import com.example.quizacademytask.dto.CourseDTO
import com.example.quizacademytask.dto.toCard
import com.example.quizacademytask.dto.toCardStack
import com.example.quizacademytask.dto.toCourse
import com.google.gson.Gson
import db.dao.CardDAO
import db.dao.CardStackDAO
import db.dao.CourseDAO
import db.entities.CardStack
import db.entities.Course
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.io.IOException

class StackListFragment : Fragment(), StackListAdapter.OnItemClickListener {

    companion object {
        private const val COURSE_ID: Long = 28
    }

    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var stacksAdapter: StackListAdapter
    private lateinit var stacksList: ArrayList<String>
    private lateinit var stackMap: HashMap<String, CardStack> //Pairing stack names and the stacks
    private lateinit var courseJSON: String
    private lateinit var idlingResource: CountingIdlingResource
    private lateinit var courseDAO: CourseDAO
    private lateinit var cardStackDAO: CardStackDAO
    private lateinit var cardDAO: CardDAO
    private lateinit var courseObj: CourseDTO
    private lateinit var appContext: Context
    private lateinit var binding: FragmentStackListBinding
    private lateinit var actionModeCallback: ActionMode.Callback
    private var actionMode: Boolean = false
    var isTablet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStackListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        //INITS
        appContext = requireActivity().applicationContext
        toolbar = binding.toolbarStackList
        recyclerView = binding.recyclerView
        stacksList = ArrayList()
        isTablet = resources.getBoolean(R.bool.isTablet)
        initArrayAdapter()
        initSwipeDeleteFunction()
        recyclerView.adapter = stacksAdapter
        stackMap = HashMap()
        courseJSON = ""
        swipeContainer = binding.swipeContainer
        idlingResource = CountingIdlingResource("API")

        //Set the toolbar
        binding.toolbarStackList.title = "Topics"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        //DATABASE
        courseDAO = App.db.courseDao()
        cardStackDAO = App.db.cardStackDAO()
        cardDAO = App.db.cardDAO()

        /*Check if course already exists in database. If not then download and insert*/
        runBlocking {
            launch {
                if (!courseDAO.isExists(COURSE_ID))
                    getRequest()
                else
                    refillStacksList()
            }
        }

        actionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                this@StackListFragment.actionMode = true
                mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.deleteSelectedButton -> {
                        stacksAdapter.deleteSelected()
                        actionMode = false
                        mode.finish()
                        return true
                    }
                    else -> return false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                stacksAdapter.emptyList()
                initSwipeDeleteFunction()
            }
        }

        //SWIPE REFRESH SETTINGS; Updates from API
        swipeContainer.setOnRefreshListener {
            getRequest()
            swipeContainer.isRefreshing = false
        }

        //SAVE INSTANCE STATE
        if (savedInstanceState != null) {
            actionMode = savedInstanceState.getBoolean("actionMode")
            stacksAdapter.selectedItems =
                savedInstanceState.getSerializable("selectedList") as HashMap<Int, String>
            if (actionMode) requireActivity().startActionMode(actionModeCallback)
            recyclerView.adapter = stacksAdapter
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
                stacksAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    /* Initialize the array adapters */
    private fun initArrayAdapter() {
        stacksAdapter = StackListAdapter(stacksList, this)
    }

    /* Create a Course from JSON */
    private fun createCourse(jsonString: String): Course {
        var course: Course? = null
        runBlocking {
            launch {
                courseObj = gsonParse(jsonString)
                val courseRowId = dbEntries(courseObj)
                course = courseDAO.getById(courseRowId)
            }
        }
        return course!!
    }

    /* Create a DTO from JSON */
    private fun gsonParse(jsonString: String): CourseDTO {
        return Gson().fromJson(jsonString, CourseDTO::class.java)
    }

    private suspend fun dbEntries(courseObj: CourseDTO): Long {
        val courseRowId = courseDAO.insert(courseObj.toCourse())

        for (stack in courseObj.cardStacks) {
            cardStackDAO.insert(stack.toCardStack(courseRowId))

            for (card in stack.cards) {
                cardDAO.insert(card.toCard(stack.id))
            }
        }
        return courseRowId
    }

    /* Send GET request to API*/
    private fun getRequest() {
        EspressoIdlingResource.increment()
        val url = "https://api.quizacademy.io/quiz-dev/public/courses/$COURSE_ID"
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
                    courseDAO.getCourseAndCardStacks(COURSE_ID)[0].cardStacks

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
        if (actionMode && v != null) {
            stacksAdapter.processSelect(position, v.findViewById<TextView?>(R.id.rowText))
            return
        }
        val item = stacksList[position]
        val stack = stackMap[item]
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

    override fun onItemLongClick(position: Int, v: View?) {
        requireActivity().startActionMode(actionModeCallback)
        onItemClick(position, v)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.stacks_update_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.updateButton) getRequest()
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("selectedList", stacksAdapter.selectedItems)
        outState.putBoolean("actionMode", actionMode)
        super.onSaveInstanceState(outState)
    }

}



