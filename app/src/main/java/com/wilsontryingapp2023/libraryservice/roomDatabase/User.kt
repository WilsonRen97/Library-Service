package com.wilsontryingapp2023.libraryservice.roomDatabase

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = arrayOf("isbn"),
        childColumns = arrayOf("bookBorrowing1"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.NO_ACTION
    ), ForeignKey(
        entity = Book::class,
        parentColumns = arrayOf("isbn"),
        childColumns = arrayOf("bookBorrowing2"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.NO_ACTION
    ), ForeignKey(
        entity = Book::class,
        parentColumns = arrayOf("isbn"),
        childColumns = arrayOf("bookBorrowing3"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.NO_ACTION
    ), ForeignKey(
        entity = Book::class,
        parentColumns = arrayOf("isbn"),
        childColumns = arrayOf("bookBorrowing4"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class User(
    @PrimaryKey val id: String,
    val fullName: String,
    val bookBorrowing1: String?,
    val bookBorrowing2: String?,
    val bookBorrowing3: String?,
    val bookBorrowing4: String?,
)

