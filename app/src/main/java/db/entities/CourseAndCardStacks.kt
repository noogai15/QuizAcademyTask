package db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CourseAndCardStacks(
    @Embedded val course: Course,
    @Relation(parentColumn = "courseId", entityColumn = "courseId")
    val cardStacks: List<CardStack>
)
