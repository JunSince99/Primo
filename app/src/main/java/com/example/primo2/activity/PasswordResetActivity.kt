package com.example.primo2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.primo2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        findViewById<Button>(R.id.checkButton).setOnClickListener{
            send()
        }
    }



    private fun send(){
        var email:String = findViewById<EditText>(R.id.emailEditText).text.toString()

        if(email.isNotEmpty()) {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "이메일을 전송했습니다.",
                            Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(baseContext, "이메일을 확인해주세요.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            Toast.makeText(baseContext, "이메일을 입력해주세요.",
                Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}

