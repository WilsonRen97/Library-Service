package com.wilsontryingapp2023.libraryservice.roomDatabase

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM user where id = :id")
    fun getUserById(id : String): User

    @Insert
    fun insertOneNewUser(user: User)

    @Update
    fun updateUsers(user: User)

    @Delete
    fun deleteOneUser(user: User)
}