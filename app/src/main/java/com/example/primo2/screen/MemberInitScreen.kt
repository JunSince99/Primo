package com.example.primo2.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography

@Composable
fun MemberInitScreen(
    onSubmitButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "정보를 입력해주세요.",
            style = Typography.h1,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        var Nickname by remember { mutableStateOf("") }

        TextField(
            value = Nickname,
            onValueChange = {
                Nickname = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
            label = {Text("닉네임")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                focusedLabelColor = Color.Black
            )
        )
        while(false){
            Text(
                text = "닉네임을 입력해주세요.",
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colors.error
            )
        }
    }
}

@Preview
@Composable
fun MemberinitPreview() {
    LazyColumnExampleTheme() {
        MemberInitScreen()
    }
}