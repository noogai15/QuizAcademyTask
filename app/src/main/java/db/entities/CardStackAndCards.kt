package db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CardStackAndCards(
    @Embedded val cardStack: CardStack,
    @Relation(parentColumn = "cardStackId", entityColumn = "cardStackId")
    val cards: List<Card>
)
