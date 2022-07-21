package db.dto

import db.entities.Card
import java.io.Serializable

data class CardDTO(
    val accepted: Boolean,
    val answer: String,
    val created_at: String,
    val created_by: String,
    val explanation: String,
    val id: Long,
    val position: Int,
    val tags: List<Any>,
    val text: String,
    val updated_at: String,
    val updated_by: String,
    val video_question_or_answer: Any,
    val weblink: String
) : Serializable

fun CardDTO.toCard(cardStackId: Long): Card = Card(id, cardStackId, answer, explanation, text)
