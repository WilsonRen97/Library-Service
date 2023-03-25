package com.wilsontryingapp2023.libraryservice.roomDatabase

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM book where isbn = :isbn")
    fun getBookByISBN(isbn : String): Book

    @Insert
    fun insertOneNewBook(book: Book)

    @Update
    fun updateOneBook(book: Book)

    @Delete
    fun deleteOneBook(book: Book)
}