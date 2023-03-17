package com.wilsontryingapp2023.libraryservice.roomDatabase

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM user where id = :id")
    suspend fun getUserById(id : String): User

    @Insert
    suspend fun insertOneNewUser(user: User)

    @Update
    suspend fun updateUsers(user: User)

    @Delete
    suspend fun deleteOneUser(user: User)
}