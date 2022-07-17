package db.dao

import androidx.room.*
import db.entities.CardStack
import db.entities.Course
import db.entities.CourseAndCardStacks

@Dao
interface CourseDAO {
    @Query("SELECT * FROM course")
    suspend fun getAll(): List<Course>

    @Query("SELECT * FROM course WHERE courseId=(:id)")
    suspend fun getById(id: Int): Course

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: Course): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardStack(stack: CardStack): Long

    @Transaction
    @Query("SELECT * FROM course WHERE courseId=(:courseId)")
    suspend fun getCourseAndCardStacks(courseId: Int): List<CourseAndCardStacks>

    @Delete
    suspend fun delete(course: Course)
}
