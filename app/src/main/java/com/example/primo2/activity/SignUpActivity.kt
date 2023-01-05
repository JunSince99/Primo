package com.example.primo2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.primo2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<Button>(R.id.checkButton).setOnClickListener{
            signUp()
        }
    }



    private fun signUp() {
        // [START sign_in_with_email]
        var email:String = findViewById<EditText>(R.id.emailEditText).text.toString();
        var password:String = findViewById<EditText>(R.id.passwordEditText).text.toString();
        var passwordCheck:String = findViewById<EditText>(R.id.passwordCheckEditText).text.toString();
        if(email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) {
            val loaderLayout: View = findViewById<View>(R.id.loader_layout)
            loaderLayout.visibility = View.VISIBLE
            if (password.equals(passwordCheck)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(
                                baseContext, "회원가입이 성공했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            loaderLayout.visibility = View.GONE
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext, "인증 오류, 회원가입에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            loaderLayout.visibility = View.GONE
                        }
                    }
                // [END sign_in_with_email]
            } else {
                Toast.makeText(
                    baseContext, "비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            Toast.makeText(
                baseContext, "이메일 또는 비밀번호를 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}

