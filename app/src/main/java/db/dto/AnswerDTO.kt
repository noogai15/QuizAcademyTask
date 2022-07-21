package db.dto

data class AnswerDTO(
    val id: Int,
    val is_right: Boolean,
    val text: String
)