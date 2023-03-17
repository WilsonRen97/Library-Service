package com.wilsontryingapp2023.libraryservice.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import com.wilsontryingapp2023.libraryservice.R
import com.wilsontryingapp2023.libraryservice.roomDatabase.AppDatabase
import com.wilsontryingapp2023.libraryservice.roomDatabase.BookDao
import com.wilsontryingapp2023.libraryservice.roomDatabase.UserDao

class MainActivity : AppCompatActivity() {
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button

    companion object {
        var userDao: UserDao? = null
        var bookDao: BookDao? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1 = findViewById(R.id.register_book_Btn)
        btn2 = findViewById(R.id.register_user_Btn)
        btn3 = findViewById(R.id.borrow_return_Btn)
        btn4 = findViewById(R.id.data_query_Btn)

        btn1.setOnClickListener {
            startActivity(Intent(this, BookRegisterActivity::class.java))
        }

        btn2.setOnClickListener {
            startActivity(Intent(this, UserRegisterActivity::class.java))
        }

        btn3.setOnClickListener {
            startActivity(Intent(this, BorrowReturnBookActivity::class.java))
        }

        btn4.setOnClickListener {
            startActivity(Intent(this, DataQueryActivity::class.java))
        }

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "library-db").build()
        userDao = db.userDao()
        bookDao = db.bookDao()
    }
}