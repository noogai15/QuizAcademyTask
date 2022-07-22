package db.dto

import com.google.gson.annotations.SerializedName
import db.entities.Card
import java.io.Serializable

data class CardDTO(
    val accepted: Boolean,
    val answer: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("created_by") val createdBy: String,
    val explanation: String,
    val id: Long,
    val position: Int,
    val tags: List<Any>,
    val text: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("updated_by") val updatedBy: String,
    @SerializedName("vide_question_or_answer") val videoQuestionOrAnswer: Any,
    val weblink: String
) : Serializable

fun CardDTO.toCard(cardStackId: Long): Card = Card(id, cardStackId, answer, explanation, text)
