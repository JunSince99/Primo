package com.example.primo2.screen

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography

@Composable
fun RegisterPass_2(
    onRegisterButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "이메일 주소를 입력해주세요.",
            style = Typography.h1,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.padding(11.dp))
        var email by remember { mutableStateOf("") }

        var isErrorInID by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.trim()
                isErrorInID = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches().not()
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            label = {Text("이메일")},
            isError = isErrorInID,
            trailingIcon = {
                if (isErrorInID)
                    Icon(Icons.Filled.Info, "Error", tint = MaterialTheme.colors.error)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
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

        Spacer(modifier = Modifier.padding(8.dp))

        var password by remember { mutableStateOf("") }
        var passwordconfirm by remember { mutableStateOf("")}
        var isSamePass by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
            label = {Text("비밀번호")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.DarkGray
            )
        )
        Text(
            text = "8자리 이상 입력해주세요.",
            modifier = Modifier.padding(start = 16.dp)
        )
        OutlinedTextField(
            value = passwordconfirm,
            onValueChange = {
                passwordconfirm = it
                isSamePass = (it != password)
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
            label = {Text("비밀번호 확인")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = isSamePass,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.DarkGray
            )
        )
    }
}

@Preview
@Composable
fun RegisterPreview2() {
    LazyColumnExampleTheme() {
        RegisterPass_2()
    }
}