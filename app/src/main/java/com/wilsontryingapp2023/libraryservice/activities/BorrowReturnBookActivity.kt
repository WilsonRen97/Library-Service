package com.wilsontryingapp2023.libraryservice.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wilsontryingapp2023.libraryservice.R
import com.wilsontryingapp2023.libraryservice.roomDatabase.Book
import com.wilsontryingapp2023.libraryservice.roomDatabase.User
import java.util.concurrent.Executors

class BorrowReturnBookActivity : AppCompatActivity() {

    private lateinit var bookISBN_for_borrow_return: EditText
    private lateinit var userId_for_borrow_return: EditText
    private lateinit var borrowBtn: Button
    private lateinit var returnBtn: Button
    private lateinit var borrow_return_result: TextView
    private var handler : Handler = Handler(Looper.getMainLooper()!!)
    private var singleThreadExecutors = Executors.newSingleThreadExecutor()

    private fun dataCheck(isbn: String, userID: String): Boolean {
        val ab: AlertDialog.Builder = AlertDialog.Builder(this)
        if (isbn == "") {
            ab.setTitle("Book ISBN Error")
            ab.setMessage("The book ISBN cannot be empty ")
            ab.setCancelable(false)
            ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
            val box: AlertDialog = ab.create()
            box.show()
            handler.post {
                borrow_return_result.text = ""
            }
            return false
        }

        if (isbn.length != 10) {
            ab.setTitle("Book ISBN Error")
            ab.setMessage("The book ISBN is too short or too long. ISBN has to be 10 digits long.")
            ab.setCancelable(false)
            ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
            val box: AlertDialog = ab.create()
            box.show()
            handler.post {
                borrow_return_result.text = ""
            }
            return false
        }

        if (!BookRegisterActivity.validationCheckForISBN(isbn)) {
            ab.setTitle("Book ISBN Validation Error")
            ab.setMessage("The book ISBN didn't pass the validation check.")
            ab.setCancelable(false)
            ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
            val box: AlertDialog = ab.create()
            box.show()
            handler.post {
                borrow_return_result.text = ""
            }
            return false
        }

        if (userID == "") {
            ab.setTitle("User ID Error")
            ab.setMessage("User ID cannot be empty.")
            ab.setCancelable(false)
            ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
            val box: AlertDialog = ab.create()
            box.show()
            handler.post {
                borrow_return_result.text = ""
            }
            return false
        }

        if (userID.length != 10) {
            ab.setTitle("User ID Error")
            ab.setMessage("The User ID is too long or too short")
            ab.setCancelable(false)
            ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
            val box: AlertDialog = ab.create()
            box.show()
            handler.post {
                borrow_return_result.text = ""
            }
            return false
        }

        if (!UserRegisterActivity.validationCheckForID(userID)) {
            ab.setTitle("User ID Validation Error")
            ab.setMessage("The user ID didn't pass the validation check. Please go back and check.")
            ab.setCancelable(false)
            ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
            val box: AlertDialog = ab.create()
            box.show()
            handler.post {
                borrow_return_result.text = ""
            }
            return false
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.borrow_return_layout)
        bookISBN_for_borrow_return = findViewById(R.id.bookISBN_for_borrow_return)
        userId_for_borrow_return = findViewById(R.id.userId_for_borrow_return)
        borrowBtn = findViewById(R.id.borrowBtn)
        returnBtn = findViewById(R.id.returnBtn)
        borrow_return_result = findViewById(R.id.borrow_return_result)

        borrowBtn.setOnClickListener {
            val isbn = bookISBN_for_borrow_return.text.toString()
            val userID = userId_for_borrow_return.text.toString().uppercase()

            if (!dataCheck(isbn, userID)) {
                return@setOnClickListener
            }

            singleThreadExecutors.execute {
                try {
                    // 找尋使用者
                    val user = MainActivity.userDao!!.getUserById(userID)
                    println("the found user is $user")
                    if (user == null) {
                        handler.post {
                            borrow_return_result.text = "User not found. Borrow failed."
                        }
                        return@execute
                    }

                    // 找尋圖書
                    val book = MainActivity.bookDao!!.getBookByISBN(isbn)
                    if (book == null) {
                        handler.post {
                            borrow_return_result.text = "Book not found. Borrow failed."
                        }
                        return@execute
                    }

                    // 如果使用者已經借了四本書，則不予以借書
                    if (user.bookBorrowing1 != null && user.bookBorrowing2 != null && user.bookBorrowing3 != null && user.bookBorrowing4 != null) {
                        handler.post {
                            borrow_return_result.text =
                                "User have reach the book borrow limit. Borrow failed."
                        }
                        return@execute
                    }

                    // 如果某本書已經借給某人，borrowedTo不是null，則代表這本書可能館員沒有刷還
                    // 或者這個人是撿到這本書等等，須了解狀況後，再予以借書
                    if (book.borrowedTo != null) {
                        handler.post {
                            borrow_return_result.text =
                                "The book is borrowed to someone already. Borrow failed"
                        }
                        return@execute
                    }

                    if (user.bookBorrowing1 == null) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                isbn,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                user.bookBorrowing4
                            )
                        )
                    } else if (user.bookBorrowing2 == null) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                user.bookBorrowing1,
                                isbn,
                                user.bookBorrowing3,
                                user.bookBorrowing4
                            )
                        )
                    } else if (user.bookBorrowing3 == null) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                isbn,
                                user.bookBorrowing4
                            )
                        )
                    } else if (user.bookBorrowing4 == null) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                isbn
                            )
                        )
                    }

                    // 更新Book的borrowTo屬性
                    MainActivity.bookDao!!.updateOneBook(
                        Book(
                            book.isbn,
                            book.bookName,
                            user.id
                        )
                    )
                    handler.post {
                        borrow_return_result.text = "Borrow Success!!"
                        bookISBN_for_borrow_return.setText("")
                        userId_for_borrow_return.setText("")
                    }
                } catch (e: Exception) {
                    handler.post {
                        borrow_return_result.text = e.message
                    }
                }
            }
        }

        returnBtn.setOnClickListener {
            val isbn = bookISBN_for_borrow_return.text.toString()
            val userID = userId_for_borrow_return.text.toString().uppercase()

            if (!dataCheck(isbn, userID)) {
                return@setOnClickListener
            }

            singleThreadExecutors.execute {
                try {
                    // 找尋使用者
                    val user = MainActivity.userDao!!.getUserById(userID)
                    if (user == null) {
                        handler.post {
                            borrow_return_result.text = "User not found. Return failed."
                        }
                        return@execute
                    }

                    // 找尋圖書
                    val book = MainActivity.bookDao!!.getBookByISBN(isbn)
                    if (book == null) {
                        handler.post {
                            borrow_return_result.text = "Book not found. Return failed."
                        }
                        return@execute
                    }

                    // 確認使用者有無借過此書
                    if (user.bookBorrowing1 != isbn && user.bookBorrowing2 != isbn && user.bookBorrowing3 != isbn && user.bookBorrowing4 != isbn) {
                        handler.post {
                            borrow_return_result.text =
                                "User didn't borrow this book before. Return failed."
                        }
                        return@execute
                    }

                    if (user.bookBorrowing1 == isbn) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                null,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                user.bookBorrowing4
                            )
                        )
                    } else if (user.bookBorrowing2 == isbn) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                user.bookBorrowing1,
                                null,
                                user.bookBorrowing3,
                                user.bookBorrowing4
                            )
                        )
                    } else if (user.bookBorrowing3 == isbn) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                null,
                                user.bookBorrowing4
                            )
                        )
                    } else if (user.bookBorrowing4 == isbn) {
                        MainActivity.userDao!!.updateUsers(
                            User(
                                user.id,
                                user.fullName,
                                user.bookBorrowing1,
                                user.bookBorrowing2,
                                user.bookBorrowing3,
                                null
                            )
                        )
                    }

                    // 更新Book的borrowTo屬性
                    MainActivity.bookDao!!.updateOneBook(
                        Book(
                            book.isbn,
                            book.bookName,
                            null
                        )
                    )
                    handler.post {
                        borrow_return_result.text = "Return Success!!"
                        bookISBN_for_borrow_return.setText("")
                        userId_for_borrow_return.setText("")
                    }
                } catch (e: Exception) {
                    handler.post {
                        borrow_return_result.text = e.message
                    }
                }
            }
        }
    }
}