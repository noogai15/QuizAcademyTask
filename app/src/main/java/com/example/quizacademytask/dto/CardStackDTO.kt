package com.example.quizacademytask.dto

import com.google.gson.annotations.SerializedName
import db.entities.CardStack
import java.io.Serializable

data class CardStackDTO(
    val active: Boolean,
    val cards: List<CardDTO>,
    @SerializedName("created_ay") val createdAt: String,
    @SerializedName("created_by") val createdBy: String,
    val id: Long,
    val name: String,
    @SerializedName("num_cards") val numCards: Int,
    val position: Int,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("updated_by") val updatedBy: String
) : Serializable

fun CardStackDTO.toCardStack(courseId: Long): CardStack = CardStack(id, courseId, name, numCards)
