package com.example.primo2.screen

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.text.Typography


@Composable
fun LoginScreen(
    onLoginButtonClicked: (Boolean) -> Unit,
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
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "이메일과 비밀번호를\n입력하세요",
            style = Typography.h1,
            modifier = Modifier
                .padding(vertical = 8.dp)
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
            //placeholder = {Text("이메일 주소 입력")},
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
                focusedLabelColor = Color.Black
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
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.Black
            )
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(20f / 4f)
                .padding(vertical = 8.dp),
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
                                        onLoginButtonClicked(isMember)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(activity, "오류 발생",
                                            Toast.LENGTH_SHORT).show()
                                    }
                            } else {
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
            Text(
                "로그인",
                fontSize = 16.sp
            )
            
        }
        Row( //회원가입 버튼
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = "아직 회원이 아니신가요?  ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            ClickableText(
                text = AnnotatedString("회원가입"),
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier,
                onClick = {
                    //회원가입 페이지로
                    onRegisterScreenButtonClicked()

                }
            )
        }
        Spacer(modifier = Modifier.padding(90.dp))
        Button( // 나중에 삭제
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                email = "11@naver.com"
                password = "123456"
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)
        ){
            Text("개발자용 자동로그인")
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
