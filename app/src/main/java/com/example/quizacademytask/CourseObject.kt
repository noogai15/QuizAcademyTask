package com.example.quizacademytask

import java.io.Serializable

data class CourseObject(
    val available_for_coursemarket: Boolean,
    val card_stacks: List<CardStackObject>,
    val category: Category,
    val created_at: String,
    val created_by: String,
    val exams: List<Any>,
    val id: Int,
    val is_active: Boolean,
    val is_password_protected: Boolean,
    val is_public: Boolean,
    val is_user_restricted: Boolean,
    val is_watched: Boolean,
    val name: String,
    val num_cards: Int,
    val num_questions: Int,
    val num_quiz: Int,
    val num_stacks: Int,
    val organization: Organization,
    val pin_code: String,
    val quizzes: List<Quiz>,
    val responsible: String,
    val score: Int,
    val type: String,
    val updated_at: String,
    val updated_by: String
)

data class CardStackObject(
    val active: Boolean,
    val cards: List<CardObject>,
    val created_at: String,
    val created_by: String,
    val id: Int,
    val name: String,
    val num_cards: Int,
    val position: Int,
    val updated_at: String,
    val updated_by: String
) : Serializable

data class Category(
    val id: Int,
    val name: String,
    val parent: Int
)

data class Organization(
    val id: Int
)

data class Quiz(
    val active: Boolean,
    val created_at: String,
    val created_by: String,
    val id: Int,
    val name: String,
    val num_questions: Int,
    val position: Int,
    val questions: List<Question>,
    val updated_at: String
)

data class CardObject(
    val accepted: Boolean,
    val answer: String,
    val created_at: String,
    val created_by: String,
    val explanation: String,
    val id: Int,
    val position: Int,
    val tags: List<Any>,
    val text: String,
    val updated_at: String,
    val updated_by: String,
    val video_question_or_answer: Any,
    val weblink: String
) : Serializable

data class Question(
    val answers: List<Answer>,
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

data class Answer(
    val id: Int,
    val is_right: Boolean,
    val text: String
)