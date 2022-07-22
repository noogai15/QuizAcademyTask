package db.dto

import com.google.gson.annotations.SerializedName
import db.entities.Course

data class CourseDTO(
    @SerializedName("available_for_coursemarket") val availableForCoursemarket: Boolean,
    @SerializedName("card_stacks") val cardStacks: List<CardStackDTO>,
    val category: CategoryDTO,
    @SerializedName("create_at") val createdAt: String,
    @SerializedName("created_by") val createdBy: String,
    val exams: List<Any>,
    val id: Long,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_password_protected") val isPasswordProtected: Boolean,
    @SerializedName("is_public") val isPublic: Boolean,
    @SerializedName("is_user_restricted") val isUserRestricted: Boolean,
    @SerializedName("is_watched") val isWatched: Boolean,
    val name: String,
    @SerializedName("num_cards") val numCards: Int,
    @SerializedName("num_questions") val numQuestions: Int,
    @SerializedName("num_quiz") val numQuiz: Int,
    @SerializedName("num_stacks") val numStacks: Int,
    val organization: OrganizationDTO,
    @SerializedName("pin_code") val pinCode: String,
    val quiz: List<QuizDTO>,
    val responsible: String,
    val score: Int,
    val type: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("updated_by") val updatedBy: String
)

fun CourseDTO.toCourse(): Course = Course(id, name, numCards, numStacks)
