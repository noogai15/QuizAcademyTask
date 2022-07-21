package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Card(
    @PrimaryKey val cardId: Long,
    @ColumnInfo(name = "cardStackId") val cardStackId: Long, //Foreign key to CardStack
//    @ColumnInfo(name = "accepted") val accepted: Boolean,
    @ColumnInfo(name = "answer") val answer: String,
//    @ColumnInfo(name = "created_at") val created_at: String,
//    @ColumnInfo(name = "created_by") val created_by: String,
    @ColumnInfo(name = "explanation") val explanation: String,
//    @ColumnInfo(name = "position") val position: Int,
//    @ColumnInfo(name = "tags") val tags: List<Any>,
    @ColumnInfo(name = "text") val text: String,
//    @ColumnInfo(name = "updated_at") val updated_at: String,
//    @ColumnInfo(name = "updated_by") val updated_by: String,
//    @ColumnInfo(name = "video_question_or_answer") val video_question_or_answer: Any,
//    @ColumnInfo(name = "weblink") val weblink: String
) : Serializable

