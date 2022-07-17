package db.dao

import androidx.room.*
import db.entities.Card
import db.entities.CardStack
import db.entities.CardStackAndCards

@Dao
interface CardStackDAO {
    @Query("SELECT * FROM cardStack")
    suspend fun getAll(): List<CardStack>

    @Query("SELECT * FROM cardStack WHERE cardStackId = :id")
    suspend fun getById(id: Long): CardStack

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cardStack: CardStack)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card): Long

    @Transaction
    @Query("SELECT * FROM cardStack WHERE cardStackId=(:stackId)")
    suspend fun getCardStackAndCards(stackId: Int): List<CardStackAndCards>

    @Delete
    suspend fun delete(cardStack: CardStack)
}