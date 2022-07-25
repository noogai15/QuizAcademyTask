package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class CardStack(
    @PrimaryKey val cardStackId: Long,
    @ColumnInfo(name = "courseId") val courseId: Long, //Foreign key to course
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "num_cards") val num_cards: Int,
) : Serializable
