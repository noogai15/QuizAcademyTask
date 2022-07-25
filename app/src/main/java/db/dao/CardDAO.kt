package db.dao

import androidx.room.*
import db.entities.Card

@Dao
interface CardDAO {
    @Query("SELECT * FROM card")
    suspend fun getAll(): List<Card>

    @Query("SELECT * FROM card WHERE cardId = :id")
    suspend fun getById(id: Long): Card

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Delete
    suspend fun delete(card: Card)
}
