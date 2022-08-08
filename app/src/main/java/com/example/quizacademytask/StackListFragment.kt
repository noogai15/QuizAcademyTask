package com.example.quizacademytask

import android.os.Bundle
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


private lateinit var swipeContainer: SwipeRefreshLayout
private lateinit var recyclerView: RecyclerView
private lateinit var idlingResource: CountingIdlingResource
private lateinit var stacksAdapter: SimpleAdapter
private lateinit var binding: FragmentStackListBinding
private lateinit var model: MainViewModel
private val backendClient: BackendClient = BackendClient()

var isTablet: Boolean = false

class StackListFragment : Fragment(), SimpleAdapter.OnItemClickListener {

    companion object {
        const val COURSE_ID = 28
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStackListBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        /*Check if course already exists in database. If not then download and insert*/
        if (model.cardStacks.isEmpty())
            handleGetCourse()

        //INITS
        recyclerView = binding.recyclerView
        isTablet = resources.getBoolean(R.bool.isTablet)
        initArrayAdapters()
        initSwipeDeleteFunction()
        recyclerView.adapter = stacksAdapter
        swipeContainer = binding.swipeContainer
        idlingResource = CountingIdlingResource("API")
        binding.toolbarStackList.title = "Topics"

        //SWIPE REFRESH SETTINGS; Updates from API
        swipeContainer.setOnRefreshListener {
            handleGetCourse()
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
        stacksAdapter = SimpleAdapter(model.stacksList, this)
    }

    private fun handleGetCourse() {
        backendClient.requestCourse(COURSE_ID, { courseJSON ->
            model.courseJSON = courseJSON
            model.createStacks()
            model.refillStacksList()
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Course downloaded",
                    Toast.LENGTH_SHORT,
                )
                    .show()
                stacksAdapter.notifyDataSetChanged()
            }
        }, {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Error downloading course",
                    Toast.LENGTH_SHORT,
                )
                    .show()
            }
        })
    }


    override fun onItemClick(position: Int, v: View?) {
        val item = model.stacksList[position]
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
