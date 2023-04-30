package com.example.primo2.screen

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography

@Composable
fun RegisterPass_2(
    onRegisterButtonClicked: (userPassword:String?) -> Unit = {},
    userEmail:String?,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ){
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(41.dp)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "비밀번호를 입력해주세요.",
                style = Typography.h1,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
            var password by remember { mutableStateOf("") }
            var isErrorinPw by remember { mutableStateOf(false) }
            var passwordconfirm by remember { mutableStateOf("")}
            var isSamePass by remember { mutableStateOf(false) }

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    isErrorinPw = (0< password.length && password.length< 8)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
                label = {Text("비밀번호")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                isError = isErrorinPw,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black
                )
            )
            if(isErrorinPw){
                Text(
                    text = "8자리 이상 입력해주세요.",
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colors.error
                )
            } else{
                Text(
                    text = "8자리 이상 입력해주세요.",
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {navController.navigate(PrimoScreen.RegisterUser.name)}
                ),
                isError = isSamePass,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.Black
                )
            )
            if (isSamePass) {
                Text(
                    text = "비밀번호가 일치하지 않습니다.",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

