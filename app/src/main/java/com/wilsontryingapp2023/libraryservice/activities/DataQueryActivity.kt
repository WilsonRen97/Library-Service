package com.wilsontryingapp2023.libraryservice.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wilsontryingapp2023.libraryservice.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DataQueryActivity : AppCompatActivity() {

    private lateinit var data_id_for_query : EditText
    private lateinit var search_for_data_Btn : Button
    private lateinit var findAllUserBtn : Button
    private lateinit var findAllBookBtn : Button
    private lateinit var data_query_result : TextView
    private var handler : Handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_query_layout)

        data_id_for_query = findViewById(R.id.data_id_for_query)
        search_for_data_Btn = findViewById(R.id.search_for_data_Btn)
        data_query_result = findViewById(R.id.data_query_result)
        findAllUserBtn = findViewById(R.id.findAllUserBtn)
        findAllBookBtn = findViewById(R.id.findAllBookBtn)

        findAllBookBtn.setOnClickListener{
            Thread {
                var result = ""
                var books = MainActivity.bookDao!!.getAllBooks()
                for (book in books) {
                    result += "Book name is ${book.bookName}\n"
                    result += "Book ISBN is ${book.isbn}\n"
                    result += "Book is now borrowed to ${book.borrowedTo}\n"
                    result += "========================="
                }

                handler.post{
                    data_query_result.text = "The result is :\n${result}"
                }
            }.start()
        }

        findAllUserBtn.setOnClickListener{
            Thread {
                var result = ""
                var users = MainActivity.userDao!!.getAllUsers()
                for (user in users) {
                    result += "User name is ${user.fullName}\n"
                    result += "User ID is ${user.id}\n"
                    result += "The books this user is borrowing:\n"
                    var isBorrowingBooks = false
                    val bookBorrowings = arrayOf(user.bookBorrowing1, user.bookBorrowing2, user.bookBorrowing3, user.bookBorrowing4)
                    for (i in bookBorrowings.indices) {
                        if (bookBorrowings[i] != null) {
                            isBorrowingBooks = true
                            val book = MainActivity.bookDao!!.getBookByISBN(bookBorrowings[i]!!)
                            result += "${book.isbn} ${book.bookName}\n"
                        }
                    }
                    if (!isBorrowingBooks) {
                        result += "none\n"
                    }
                    result += "============\n"
                }

                handler.post{
                    data_query_result.text = "The result is :\n${result}"
                }
            }.start()
        }


        search_for_data_Btn.setOnClickListener{
            val id = data_id_for_query.text.toString()
            if (id.length != 10) {
                handler.post {
                    data_query_result.text = "The book ISBN or User ID is too long or too short."
                }
                return@setOnClickListener
            }

            Thread {
                try {
                    var result = ""
                    // 使用者提供的是身分證字號，所以要查詢使用者資訊
                    if (id[0].isLetter()) {
                        val user = MainActivity.userDao!!.getUserById(id.uppercase())
                        if (user != null) {
                            result += "User name is ${user.fullName}\n"
                            result += "User ID is ${user.id}\n"
                            result += "The books this user is borrowing:\n"
                            var isBorrowingBooks = false
                            val bookBorrowings = arrayOf(user.bookBorrowing1, user.bookBorrowing2, user.bookBorrowing3, user.bookBorrowing4)
                            for (i in bookBorrowings.indices) {
                                if (bookBorrowings[i] != null) {
                                    isBorrowingBooks = true
                                    val book = MainActivity.bookDao!!.getBookByISBN(bookBorrowings[i]!!)
                                    result += "${book.isbn} ${book.bookName}\n"
                                }
                            }
                            if (!isBorrowingBooks) {
                                result += "none\n"
                            }
                        } else {
                            result += "User not found"
                        }
                    } else {
                        println(id)
                        val book = MainActivity.bookDao!!.getBookByISBN(id)
                        result += if (book != null) {
                            "${book.isbn} ${book.bookName}\n"
                        } else {
                            "Book not found"
                        }
                    }


                    handler.post{
                        data_query_result.text = "The result is :\n${result}"
                    }
                } catch (e : Exception) {
                    println("============================")
                    println(e)
                    handler.post{
                        data_query_result.text = "The result is :\n" + e.message
                    }
                }
            }.start()
        }
    }
}