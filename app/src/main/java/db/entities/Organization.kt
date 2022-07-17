package db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Organization(
    @PrimaryKey val id: Int
) : Serializable
