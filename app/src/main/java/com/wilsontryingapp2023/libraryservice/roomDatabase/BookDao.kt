package com.wilsontryingapp2023.libraryservice.roomDatabase

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM book where isbn = :isbn")
    suspend fun getBookByISBN(isbn : String): Book

    @Insert
    suspend fun insertOneNewBook(book: Book)

    @Update
    suspend fun updateOneBook(book: Book)

    @Delete
    suspend fun deleteOneBook(book: Book)
}