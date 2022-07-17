package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Answer(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "is_right") val is_right: Boolean,
    @ColumnInfo(name = "text") val text: String
) : Serializable
