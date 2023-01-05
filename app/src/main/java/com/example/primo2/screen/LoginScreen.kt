package com.example.primo2.screen

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun LoginScreen(
    onLoginButtonClicked: (Boolean, ArrayList<PostInfo>) -> Unit,
    onRegisterScreenButtonClicked: () -> Unit,
    auth: FirebaseAuth,
    activity: Activity,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(text = "로그인 화면", style = MaterialTheme.typography.h4)

        var email by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") }
        )

        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("패스워드") }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if(email.isNotEmpty() && password.isNotEmpty()){
                    auth.signInWithEmailAndPassword(email, password) // 유저 로그인
                        .addOnCompleteListener(activity) {task->
                            if (task.isSuccessful) {
                                var isMember:Boolean = false
                                val user = auth.currentUser
                                val db = Firebase.firestore
                                val docRef = db.collection("users").document(user!!.uid)
                                docRef.get()// 유저 정보 불러오기
                                    .addOnSuccessListener { document ->
                                        if (document != null) {
                                            if (document.exists()) {
                                                isMember = true // 멤버정보 o
                                            }
                                        }

                                        val postList:ArrayList<PostInfo> = arrayListOf() // 홈화면에 띄울거 불러오기
                                        db.collection("posts")
                                            //.whereEqualTo("capital", true)
                                            .get()
                                            .addOnSuccessListener { documents ->
                                                for (pDocument in documents) {
                                                    postList.add(
                                                        PostInfo(
                                                            pDocument.getString("title"),
                                                            pDocument.data["contents"] as ArrayList<String?>,
                                                            pDocument.get("comments").toString(),
                                                            pDocument.getString("writer"),
                                                            pDocument.getString("postDate")
                                                        )
                                                    )
                                                }
                                                onLoginButtonClicked(isMember,postList)
                                            }

                                            .addOnFailureListener { exception ->
                                                Toast.makeText(activity, "오류 발생",
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(activity, "오류 발생",
                                            Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(activity, "아이디 혹은 비밀번호를 확인하세요.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else{
                    Toast.makeText(activity, "아이디 혹은 비밀번호를 입력하세요.",
                        Toast.LENGTH_SHORT).show()
                }

            }
        ) {
            Text("로그인")
        }
    }

}

@Composable
fun InputIDZone() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("ID") }
    )
}

