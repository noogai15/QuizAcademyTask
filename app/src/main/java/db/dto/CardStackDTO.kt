package db.dto

import db.entities.CardStack
import java.io.Serializable

data class CardStackDTO(
    val active: Boolean,
    val cards: List<CardDTO>,
    val created_at: String,
    val created_by: String,
    val id: Long,
    val name: String,
    val num_cards: Int,
    val position: Int,
    val updated_at: String,
    val updated_by: String
) : Serializable

fun CardStackDTO.toCardStack(courseId: Long): CardStack = CardStack(id, courseId, name, num_cards)
