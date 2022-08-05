package com.example.quizacademytask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizacademytask.dto.CourseDTO
import com.example.quizacademytask.dto.toCard
import com.example.quizacademytask.dto.toCardStack
import com.google.gson.Gson
import db.entities.CardStack
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    public val stacksList by lazy { MutableLiveData<ArrayList<String>>().also { refillStacksList() } }
    val stackMap by lazy { HashMap<String, CardStack>() }
    private val cardDAO by lazy { App.db.cardDAO() }
    private val cardStackDAO by lazy { App.db.cardStackDAO() }
    public lateinit var cardStacks: List<CardStack>
    private lateinit var courseObj: CourseDTO
    public var courseJSON: String = ""

    private fun fetchCardStacks() {
        var result: List<CardStack>
        runBlocking {
            launch {
                cardStacks = cardStackDAO.getAll()
            }
        }
    }

    @JvmName("getStacksList1")
    fun getStacksList(): ArrayList<String> = stacksList.value!!

    /* Refill the card stacks list */
    private fun refillStacksList() {
        for (i in 0 until stacksList.value!!.size) {
            stacksList.value!!.removeAt(i)
        }
        runBlocking {
            launch {
                cardStacks = cardStackDAO.getAll()
                for (stack in cardStacks) {
                    stacksList.value!!.add(stack.name)
                    stackMap[stack.name] = stack
                }
                stacksList.value!!.sortBy { it } //Alphabetical sort
            }
        }
    }

    /* Create a CardStack's from JSON */
    fun createStacks(jsonString: String): List<CardStack>? {
        var cardStacks: List<CardStack>? = null
        runBlocking {
            launch {
                courseObj = gsonParse(jsonString)
                cardStacks = dbEntries(courseObj)
            }
        }
        return cardStacks
    }

    /* Create a DTO from JSON */
    private fun gsonParse(jsonString: String): CourseDTO {
        return Gson().fromJson(jsonString, CourseDTO::class.java)
    }

    private suspend fun dbEntries(courseObj: CourseDTO): List<CardStack> {
        for (stack in courseObj.cardStacks) {
            cardStackDAO.insert(stack.toCardStack())
            for (card in stack.cards) {
                cardDAO.insert(card.toCard(stack.id))
            }
        }
        return cardStackDAO.getAll()
    }
}
