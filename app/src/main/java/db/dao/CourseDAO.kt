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
    suspend fun getById(id: Long): Course

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: Course): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardStack(stack: CardStack): Long

    @Transaction
    @Query("SELECT * FROM course WHERE courseId=(:courseId)")
    suspend fun getCourseAndCardStacks(courseId: Long): List<CourseAndCardStacks>

    @Query("SELECT EXISTS(SELECT * FROM course WHERE courseId = :courseId)")
    suspend fun isExists(courseId: Long): Boolean

    @Delete
    suspend fun delete(course: Course)

    @Query("DELETE from course WHERE courseId = :courseId")
    suspend fun deleteById(courseId: Long)

}
