package com.wilsontryingapp2023.libraryservice.roomDatabase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = arrayOf(ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("borrowedTo"),
        onUpdate=ForeignKey.CASCADE,
        onDelete = ForeignKey.NO_ACTION)))
data class Book(
    @PrimaryKey val isbn: String,
    val bookName: String,
    val borrowedTo: String? // borrowedTo can be null, so we use String?
)