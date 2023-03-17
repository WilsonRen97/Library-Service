package com.wilsontryingapp2023.libraryservice.roomDatabase



import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
}