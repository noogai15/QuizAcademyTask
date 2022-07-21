package db.dto

data class QuestionDTO(
    val answerDTOS: List<AnswerDTO>,
    val created_at: String,
    val created_by: String,
    val explanation: String,
    val id: Int,
    val is_poll_question: Boolean,
    val is_right: Boolean,
    val num_answers: Int,
    val position: Int,
    val tags: List<Any>,
    val text: String,
    val type: Int,
    val updated_at: String,
    val updated_by: String,
    val weblink: String
)