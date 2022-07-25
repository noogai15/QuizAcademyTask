package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Course(
    @PrimaryKey val courseId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "num_cards") val num_cards: Int?,
    @ColumnInfo(name = "num_stacks") val num_stacks: Int?,
) : Serializable
