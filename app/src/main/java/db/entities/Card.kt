package db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Card(
    @PrimaryKey val cardId: Long,
    @ColumnInfo(name = "cardStackId") val cardStackId: Long, //Foreign key to CardStack
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "explanation") val explanation: String,
    @ColumnInfo(name = "text") val text: String,
) : Serializable
