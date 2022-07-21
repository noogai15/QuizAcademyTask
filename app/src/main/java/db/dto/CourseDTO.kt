package com.example.quizacademytask

import db.dto.CardStackDTO
import db.dto.CategoryDTO
import db.dto.OrganizationDTO
import db.dto.QuizDTO
import db.entities.Course

data class CourseDTO(
    val available_for_coursemarket: Boolean,
    val card_stacks: List<CardStackDTO>,
    val categoryDTO: CategoryDTO,
    val created_at: String,
    val created_by: String,
    val exams: List<Any>,
    val id: Long,
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
    val organizationDTO: OrganizationDTO,
    val pin_code: String,
    val quizDTOS: List<QuizDTO>,
    val responsible: String,
    val score: Int,
    val type: String,
    val updated_at: String,
    val updated_by: String
)

fun CourseDTO.toCourse(): Course = Course(id, name, num_cards, num_stacks)
