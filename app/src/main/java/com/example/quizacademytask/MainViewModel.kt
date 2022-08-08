package com.example.quizacademytask

import androidx.lifecycle.ViewModel
import com.example.quizacademytask.dto.CourseDTO
import com.example.quizacademytask.dto.toCard
import com.example.quizacademytask.dto.toCardStack
import com.google.gson.Gson
import db.entities.CardStack
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    val stacksList by lazy { ArrayList<String>() }
    val stackMap by lazy { HashMap<String, CardStack>() }
    private val cardDAO by lazy { App.db.cardDAO() }
    private val cardStackDAO by lazy { App.db.cardStackDAO() }
    lateinit var cardStacks: List<CardStack>
    private lateinit var courseObj: CourseDTO
    var courseJSON: String = ""

    init {
        fetchCardStacks()
        refillStacksList()
    }

    private fun fetchCardStacks() {
        runBlocking { launch { cardStacks = cardStackDAO.getAll() } }
    }

    /* Refill the card stacks list */
    fun refillStacksList() {
        stacksList.clear()
        runBlocking {
            launch {
                cardStacks = cardStackDAO.getAll()
                for (stack in cardStacks) {
                    stacksList.add(stack.name)
                    stackMap[stack.name] = stack
                }
                stacksList.sortBy { it } //Alphabetical sort
            }
        }
    }

    /* Create a CardStack's from JSON */
    fun createStacks(): List<CardStack>? {
        var cardStacks: List<CardStack>? = null
        runBlocking {
            launch {
                courseObj = gsonParse(courseJSON)
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
