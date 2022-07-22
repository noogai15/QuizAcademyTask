package db.dto

import com.google.gson.annotations.SerializedName

data class QuizDTO(
    val active: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("created_by") val createdBy: String,
    val id: Int,
    val name: String,
    @SerializedName("num_questions") val numQuestions: Int,
    val position: Int,
    val questions: List<QuestionDTO>,
    @SerializedName("updated_at") val updatedAt: String
)