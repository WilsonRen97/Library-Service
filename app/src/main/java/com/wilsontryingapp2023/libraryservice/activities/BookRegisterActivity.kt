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
import java.util.concurrent.Executors

class BookRegisterActivity : AppCompatActivity() {

    private lateinit var bookName: EditText
    private lateinit var bookISBN: EditText
    private lateinit var bookRegisterBtn: Button
    private lateinit var bookRegisterResult : TextView
    private var handler : Handler = Handler(Looper.getMainLooper()!!)
    private var singleThreadExecutors = Executors.newSingleThreadExecutor()

    companion object {
        fun validationCheckForISBN(isbn: String): Boolean {
            var sum = 0
            for (i in 0 until isbn.length - 1) { //  注意，這裡是until length - 1，因為最後一碼是check digit
                sum += (isbn[i] - '0') * (i + 1) // isbn[i] - '0' 可以知道isbn[i]與'0'在ASCII的位置差距，也就可以算出isbn[i]的數字表示了
            }

            val checkDigitCorrect = sum % 11
            val checkDigitProvided = isbn[isbn.length - 1]
            var checkDigitProvidedToInt = 0
            //  this means the last digit is 10
            checkDigitProvidedToInt = if (checkDigitProvided == 'X') {
                10
            } else {
                checkDigitProvided - '0'
            }

            println(checkDigitProvidedToInt)
            println(checkDigitCorrect)
            return checkDigitProvidedToInt == checkDigitCorrect
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_register_layout)

        bookName = findViewById(R.id.bookName_for_register)
        bookISBN = findViewById(R.id.bookISBN_for_register)
        bookRegisterBtn = findViewById(R.id.submit_book_register)
        bookRegisterResult = findViewById(R.id.book_register_result)

        bookRegisterBtn.setOnClickListener {
            val ab: AlertDialog.Builder = AlertDialog.Builder(this)
            if (bookName.text.toString() == "") {
                ab.setTitle("Book Title Error")
                ab.setMessage("The book title cannot be empty. Please make sure you enter the book name.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
                // When return is used inside a lambda expression, it returns from the lambda expression itself
                // no tht onCreate() method
            }

            if (bookISBN.text.toString() == "") {
                ab.setTitle("Book ISBN Error")
                ab.setMessage("The book ISBN cannot be empty. Please make sure you enter the book ISBN.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            if (bookISBN.text.toString().length != 10) {
                ab.setTitle("Book ISBN Error")
                ab.setMessage("The book ISBN is too short or too long. ISBN has to be 10 digits long.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            if (!BookRegisterActivity.validationCheckForISBN(bookISBN.text.toString())) {
                ab.setTitle("Book ISBN Validation Error")
                ab.setMessage("The book ISBN didn't pass the validation check.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            singleThreadExecutors.execute{
                try {
                    val bookTitle = bookName.text.toString()
                    val bookISBNEntry = bookISBN.text.toString()
                    MainActivity.bookDao!!.insertOneNewBook(Book(bookISBNEntry, bookTitle, null))
                    handler.post{
                        bookRegisterResult.text = "The result is :\n Congrats!! The book has been registered."
                        bookName.setText("")
                        bookISBN.setText("")
                    }
                } catch (e : Exception) {
                    handler.post{
                        bookRegisterResult.text = "The result is :\n" + e.message
                    }
                }
            }
        }
    }
}