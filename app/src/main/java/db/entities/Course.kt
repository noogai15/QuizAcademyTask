package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Course(
    @PrimaryKey val courseId: Long,
//    @ColumnInfo(name = "available") val available_for_coursemarket: Boolean,
//    @ColumnInfo(name = "card_stacks") val card_stacks: List<CardStack>?,
//    @ColumnInfo(name = "category") val category: Category?,
//    @ColumnInfo(name = "created_at") val created_at: String,
//    @ColumnInfo(name = "created_by") val created_by: String,
//    @ColumnInfo(name = "exams") val exams: List<Any>?,
//    @ColumnInfo(name = "is_active") val is_active: Boolean,
//    @ColumnInfo(name = "is_password_protected") val is_password_protected: Boolean,
//    @ColumnInfo(name = "is_public") val is_public: Boolean,
//    @ColumnInfo(name = "is_user_restricted") val is_user_restricted: Boolean,
//    @ColumnInfo(name = "is_watched") val is_watched: Boolean,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "num_cards") val num_cards: Int?,
//    @ColumnInfo(name = "num_questions") val num_questions: Int?,
//    @ColumnInfo(name = "num_quiz") val num_quiz: Int?,
    @ColumnInfo(name = "num_stacks") val num_stacks: Int?,
//    @ColumnInfo(name = "organization") val organization: Organization,
//    @ColumnInfo(name = "pin_code") val pin_code: String?,
//    @ColumnInfo(name = "quizzes") val quizzes: List<Quiz>?,
//    @ColumnInfo(name = "responsible") val responsible: String?,
//    @ColumnInfo(name = "score") val score: Int?,
//    @ColumnInfo(name = "type") val type: String,
//    @ColumnInfo(name = "updated_at") val updated_at: String,
//    @ColumnInfo(name = "updated_by") val updated_by: String
) : Serializable




