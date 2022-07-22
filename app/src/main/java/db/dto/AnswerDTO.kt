package db.dto

import com.google.gson.annotations.SerializedName

data class AnswerDTO(
    val id: Int,
    @SerializedName("is_right")   val isRight: Boolean,
    val text: String
)