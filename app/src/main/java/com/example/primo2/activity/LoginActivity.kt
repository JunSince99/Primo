package com.example.primo2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.primo2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.checkButton).setOnClickListener{
            login()
        }

        findViewById<Button>(R.id.gotoSignUpButton).setOnClickListener{
            gotoSignUp()
        }

        findViewById<Button>(R.id.passwordResetbutton).setOnClickListener(){
            gotoResetPassword()
        }

    }

    private fun login() {
        // [START sign_in_with_email]
        val email:String = findViewById<EditText>(R.id.emailEditText).text.toString();
        val password:String = findViewById<EditText>(R.id.passwordEditText).text.toString();
        if(email.isNotEmpty() && password.isNotEmpty()) {
            val loaderLayout: View = findViewById<View>(R.id.loader_layout)
            loaderLayout.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        loaderLayout.visibility = View.GONE
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)

                    } else {
                        loaderLayout.visibility = View.GONE
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "아이디 혹은 비밀번호를 확인하세요.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
            Toast.makeText(
                baseContext, "이메일 혹은 비밀번호를 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun gotoSignUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
    private fun gotoResetPassword(){
        val intent = Intent(this, PasswordResetActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed(){
        super.onBackPressed()
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(1)
    }
    companion object {
        private const val TAG = "EmailPassword"
    }
}

