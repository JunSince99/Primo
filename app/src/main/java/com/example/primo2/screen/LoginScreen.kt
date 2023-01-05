package com.example.primo2.screen

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
import com.example.primo2.ui.theme.LazyColumnExampleTheme
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
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = "이메일과 비밀번호를\n입력하세요",
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 24.dp)
        )

        var email by remember { mutableStateOf("") }

        var isErrorInID by remember { mutableStateOf(false) }

        TextField(
            value = email,
            onValueChange = {
                email = it.trim()
                isErrorInID = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches().not()
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            label = {Text("이메일")},
            placeholder = {Text("이메일 주소 입력")},
            isError = isErrorInID,
            singleLine = true,
            trailingIcon = {
                if (isErrorInID)
                    Icon(Icons.Filled.Info, "Error", tint = MaterialTheme.colors.error)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.DarkGray
            )
        )
        if (isErrorInID) {
            Text(
                text = "잘못된 유형의 이메일 주소입니다.",
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        var password by remember { mutableStateOf("") }

        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
            label = {Text("비밀번호")},
            placeholder = {Text("8자리 이상 입력")},
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.DarkGray
            )
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

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White),
            enabled = !isErrorInID && password.isNotEmpty()
        ) {
            Text("로그인")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                email = "11@naver.com"
                password = "123456"
            }
        ){
            Text("빡쳐서 만든 자동로그인")
        }
    }

}

//@Composable
//fun InputIDZone() {
//    var text by remember { mutableStateOf("") }
//
//    OutlinedTextField(
//        value = text,
//        onValueChange = { text = it },
//        label = { Text("ID") }
//    )
//}


//@Preview
//@Composable
//fun LoginScreenPre(){
//    LazyColumnExampleTheme() {
//        LoginScreen(auth = )
//    }
//}
