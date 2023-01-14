package com.example.primo2.screen

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
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
fun RegisterEmail_1( // focus 처리
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
    }
}



@Preview
@Composable
fun RegisterPreview() {
    LazyColumnExampleTheme() {
        RegisterEmail_1()
    }
}