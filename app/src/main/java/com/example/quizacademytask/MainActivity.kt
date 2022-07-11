package com.example.quizacademytask

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.quizacademytask.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import java.io.*

class MainActivity : AppCompatActivity(), SimpleAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var course: Course
    private lateinit var file: File
    private lateinit var courseAdapter: SimpleAdapter
    private lateinit var stacksAdapter: SimpleAdapter
    private lateinit var courseList: ArrayList<String>
    private lateinit var stacksList: ArrayList<String>
    private lateinit var stackMap: HashMap<String, CardStack> //Pairing stack names and the stacks
    private lateinit var courseJSON: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        //INITS
        recyclerView = findViewById(R.id.recyclerView)
        courseList = ArrayList<String>()
        stacksList = ArrayList<String>()
        stackMap = HashMap<String, CardStack>()
        courseJSON = ""
        swipeContainer = findViewById(R.id.swipeContainer)

        //FILL LISTS
        courseList.add("BWL Grundlagen")

        initArrayAdapters()
        recyclerView.adapter = courseAdapter

        //SWIPE REFRESH SETTINGS
        swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (this.recyclerView.adapter == stacksAdapter) {
                refillStacksList(courseJSON)
                stacksAdapter.notifyDataSetChanged()
            }
            swipeContainer.isRefreshing = false
        })

        //SaveInstanceState
        if (savedInstanceState != null) {
            savedInstanceState.getString("courseJSON")?.let {
                courseJSON = it
            }
            savedInstanceState.getStringArrayList("courseList")?.let {
                courseList = it
            }
            savedInstanceState.getStringArrayList("stacksList")?.let {
                stacksList = it
            }
            initArrayAdapters()
            savedInstanceState.getBoolean("hasStacksAdapter").let {
                if (it) recyclerView.adapter = stacksAdapter
                else recyclerView.adapter = courseAdapter
            }
            savedInstanceState.getSerializable("stackMap")?.let {
                stackMap = it as HashMap<String, CardStack>
            }
            initSwipeDeleteFunction()
        }


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

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (recyclerView.adapter != stacksAdapter) return 0
                return super.getSwipeDirs(recyclerView, viewHolder)
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    /* Initialize the array adapters */
    private fun initArrayAdapters() {
        courseAdapter = SimpleAdapter(courseList, this, "courseAdapter")
        stacksAdapter = SimpleAdapter(stacksList, this, "stacksAdapter")
    }

    override fun onBackPressed() {
        recyclerView.adapter = courseAdapter
    }

    /* Create a Course from JSON */
    private fun createCourse(jsonString: String): Course {
        val course = Gson().fromJson(jsonString, Course::class.java);
        this.course = course
        return course
    }

    /* Read JSON from file */
    @Throws(JsonSyntaxException::class)
    fun readJSON(file: File): String {
        val bReader: BufferedReader = BufferedReader(FileReader(file))
        val content = bReader.readLines()
        val s: StringBuilder = StringBuilder()
        for (line in content)
            if (!line.isEmpty()) s.append(line)
        bReader.close()
        createCourse(s.toString())
        return s.toString()
    }

    /* Write JSON file to storage */
    fun writeJSON(fileName: String, jsonString: String): File {
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (!dir?.exists()!!) dir.mkdir()
        val file: File = File(dir.path, "$fileName.json")

        try {
            val bWriter = BufferedWriter(FileWriter(file))
            bWriter.write(jsonString)
            bWriter.close()
        } catch (e: Exception) {
            Log.e("JSON", "Error writing JSON")
        }
        this.file = file
        return file
    }

    /* Send GET request to API and switch adapter to card stacks */
    private fun getRequest() {
        val url = "https://api.quizacademy.io/quiz-dev/public/courses/28"
        val client = OkHttpClient()
        val ai: ApplicationInfo = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )
        val value: String = ai.metaData["apiKey"] as String

        val request: Request = Request.Builder()
            .header("x-api-key", value)
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@MainActivity,
                        "Error downloading course",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                })
                Log.e("JSON", e.toString())
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unsuccessful response")
                    val json: String? = response.body?.string()
                    val file = writeJSON(
                        "BWL_Grundlagen",
                        json.toString()
                    )
                    courseJSON = readJSON(file)
                    runOnUiThread(Runnable {
                        Toast.makeText(this@MainActivity, "Downloaded course", Toast.LENGTH_SHORT)
                            .show()
                        refillStacksList(courseJSON)
                        recyclerView.adapter = stacksAdapter
                        initSwipeDeleteFunction()
                    }
                    )
                }
            }
        })
    }

    /* Refill the card stacks list */
    fun refillStacksList(jsonString: String): SimpleAdapter {
        stacksList.clear()
        val course = createCourse(jsonString)

        for (stack in course.card_stacks) {
            stacksList.add(stack.name)
            stackMap[stack.name] = stack
        }
        stacksList.sortBy { it } //Alphabetical sort
        return stacksAdapter
    }

    /* OnClickListener for the course list, navigate to the flashcards if clicked on a card stack */
    override fun onItemClick(position: Int, v: View?) {
        if (recyclerView.adapter == courseAdapter) {
            getRequest()
        } else if (recyclerView.adapter == stacksAdapter) {
            val item = stacksList[position]
            val stack = stackMap[item]
            val intent: Intent = Intent(this, FlashcardStackActivity::class.java).apply {
                putExtra("stack", stack)
            }
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("courseJSON", courseJSON)
        outState.putBoolean("hasStacksAdapter", recyclerView.adapter == stacksAdapter)
        outState.putStringArrayList("stacksList", stacksList)
        outState.putStringArrayList("courseList", courseList)
        outState.putSerializable("stackMap", stackMap)
    }

}
