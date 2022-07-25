package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quiz(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "active") val active: Boolean,
    @ColumnInfo(name = "created_at") val created_at: String,
    @ColumnInfo(name = "created_by") val created_by: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "num_questions") val num_questions: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "updated_at") val updated_at: String
)

