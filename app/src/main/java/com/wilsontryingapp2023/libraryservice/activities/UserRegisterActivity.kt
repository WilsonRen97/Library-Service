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
import com.wilsontryingapp2023.libraryservice.roomDatabase.User
import java.util.concurrent.Executors

class UserRegisterActivity : AppCompatActivity() {

    private lateinit var userName: EditText
    private lateinit var userID: EditText
    private lateinit var userRegisterBtn: Button
    private lateinit var userRegisterResult: TextView
    private var handler: Handler = Handler(Looper.getMainLooper()!!)
    private var singleThreadExecutors = Executors.newSingleThreadExecutor()

    companion object {
        fun validationCheckForID(id: String): Boolean {
            if (!id[0].isLetter()) {
                return false
            }
            // rules can be found at https://wisdom-life.in/article/taiwain-id-explanation
            val letterValueMap = HashMap<Char, String>()
            val id = id.uppercase()

            letterValueMap['A'] = "10"
            letterValueMap['B'] = "11"
            letterValueMap['C'] = "12"
            letterValueMap['D'] = "13"
            letterValueMap['E'] = "14"
            letterValueMap['F'] = "15"
            letterValueMap['G'] = "16"
            letterValueMap['H'] = "17"
            letterValueMap['I'] = "34"
            letterValueMap['J'] = "18"
            letterValueMap['K'] = "19"
            letterValueMap['M'] = "21"
            letterValueMap['N'] = "22"
            letterValueMap['O'] = "35"
            letterValueMap['P'] = "23"
            letterValueMap['Q'] = "24"
            letterValueMap['T'] = "27"
            letterValueMap['U'] = "28"
            letterValueMap['V'] = "29"
            letterValueMap['W'] = "32"
            letterValueMap['X'] = "30"
            letterValueMap['Z'] = "33"

            val weight = arrayOf(1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1)
            var sum = 0
            sum += (letterValueMap[id[0]]!![0] - '0') * weight[0]
            sum += (letterValueMap[id[0]]!![1] - '0') * weight[1]
            for (i in 1 until id.length) {
                sum += (id[i] - '0') * weight[i + 1]
            }

            println(sum)
            return sum % 10 == 0
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_register_layout)

        userName = findViewById(R.id.userName_for_register)
        userID = findViewById(R.id.userId_for_register)
        userRegisterBtn = findViewById(R.id.submit_user_register_Btn)
        userRegisterResult = findViewById(R.id.user_register_result)

        userRegisterBtn.setOnClickListener {
            val ab: AlertDialog.Builder = AlertDialog.Builder(this)
            if (userName.text.toString() == "") {
                ab.setTitle("User Name Error")
                ab.setMessage("User name cannot be empty.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            if (userID.text.toString() == "") {
                ab.setTitle("User ID Error")
                ab.setMessage("User ID cannot be empty.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            if (userID.text.toString().length != 10) {
                ab.setTitle("User ID Error")
                ab.setMessage("The User ID is too long or too short")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            if (!validationCheckForID(userID.text.toString())) {
                ab.setTitle("User ID Validation Error")
                ab.setMessage("The user ID didn't pass the validation check. Please go back and check.")
                ab.setCancelable(false)
                ab.setPositiveButton("Okay") { dialog, _ -> dialog.dismiss() }
                val box: AlertDialog = ab.create()
                box.show()
                return@setOnClickListener
            }

            singleThreadExecutors.execute{
                try {
                    val userNameRegister = userName.text.toString()
                    val userIDRegister = userID.text.toString()
                    MainActivity.userDao!!.insertOneNewUser(
                        User(
                            userIDRegister,
                            userNameRegister,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                    handler.post {
                        userRegisterResult.text =
                            "The result is :\n Congrats!! The user has been registered."
                        userName.setText("")
                        userID.setText("")
                    }
                } catch (e: Exception) {
                    handler.post {
                        userRegisterResult.text = "The result is :\n" + e.message
                    }
                }
            }
        }
    }
}