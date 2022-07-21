package db.dto

data class QuizDTO(
    val active: Boolean,
    val created_at: String,
    val created_by: String,
    val id: Int,
    val name: String,
    val num_questions: Int,
    val position: Int,
    val questionDTOS: List<QuestionDTO>,
    val updated_at: String
)