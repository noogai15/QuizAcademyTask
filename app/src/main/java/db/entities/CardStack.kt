package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class CardStack(
    @PrimaryKey val cardStackId: Long,
    @ColumnInfo(name = "courseId") val courseId: Long, //Foreign key to course
//    @ColumnInfo(name = "active") val active: Boolean,
//    @ColumnInfo(name = "cards") val cards: List<Card>,
//    @ColumnInfo(name = "created_at") val created_at: String,
//    @ColumnInfo(name = "created_by") val created_by: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "num_cards") val num_cards: Int,
//    @ColumnInfo(name = "position") val position: Int?,
//    @ColumnInfo(name = "updated_at") val updated_at: String,
//    @ColumnInfo(name = "updated_by") val updated_by: String
) : Serializable
