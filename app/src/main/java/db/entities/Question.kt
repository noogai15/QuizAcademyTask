package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import db.entities.Answer
import java.io.Serializable

@Entity
data class Question(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "created_at") val created_at: String,
    @ColumnInfo(name = "created_by") val created_by: String,
    @ColumnInfo(name = "explanation") val explanation: String,
    @ColumnInfo(name = "is_poll_question") val is_poll_question: Boolean,
    @ColumnInfo(name = "is_right") val is_right: Boolean,
    @ColumnInfo(name = "num_answers") val num_answers: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "tags") val tags: List<Any>,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "updated_at") val updated_at: String,
    @ColumnInfo(name = "updated_by") val updated_by: String,
    @ColumnInfo(name = "weblink") val weblink: String
) : Serializable
