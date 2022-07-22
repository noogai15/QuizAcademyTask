package db.dto

import com.google.gson.annotations.SerializedName

data class QuestionDTO(
    val answers: List<AnswerDTO>,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("created_by") val createdBy: String,
    val explanation: String,
    val id: Int,
    @SerializedName("is_poll_question") val isPollQuestion: Boolean,
    @SerializedName("is_right") val isRight: Boolean,
    @SerializedName("num_answers") val numAnswers: Int,
    val position: Int,
    val tags: List<Any>,
    val text: String,
    val type: Int,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("updated_by") val updatedBy: String,
    val weblink: String
)