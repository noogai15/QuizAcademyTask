package db.dto

data class CategoryDTO(
    val id: Int,
    val name: String,
    val parent: Int
)