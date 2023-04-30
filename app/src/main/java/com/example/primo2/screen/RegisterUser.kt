package com.example.primo2.screen

import android.util.Patterns
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primo2.ui.theme.Typography
import com.naver.maps.map.compose.CircleOverlay

@Composable
fun RegisterUser(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
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
            ClickableText(
                text = AnnotatedString(text = "다음"),
                onClick = {},
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "정보를 입력해주세요.",
                style = Typography.h1,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            //프로필사진
            Column(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color.LightGray)
                    .size(100.dp)
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }
            Text(
                text = "프로필 사진 설정하기",
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            //이름
            var username by remember { mutableStateOf("") }
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                placeholder = { Text("이름") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    focusedLabelColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(20)
            )
            //생년월일
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                var Useryear by remember { mutableStateOf("") }
                var Usermonth by remember { mutableStateOf("") }
                var Userday by remember { mutableStateOf("") }

                var isnotyear by remember { mutableStateOf(false) }
                var isnotmonth by remember { mutableStateOf(false) }
                var isnotday by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = Useryear,
                    onValueChange = {
                        Useryear = it
                        if(Useryear.isNotEmpty()) {
                            isnotyear = Useryear.toInt() < 1900 || Useryear.toInt() > 2100
                        }
                    },
                    modifier = Modifier
                        .weight(2f),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    placeholder = { Text("YYYY") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        errorIndicatorColor = Color.Red
                    ),
                    isError = isnotyear,
                    shape = RoundedCornerShape(20)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                OutlinedTextField(
                    value = Usermonth,
                    onValueChange = {
                        Usermonth = it
                        if(Usermonth.isNotEmpty()) {
                            isnotmonth = Usermonth.toInt() > 13 || Usermonth.toInt() < 0
                        }
                    },
                    modifier = Modifier
                        .weight(1f),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    placeholder = { Text("MM") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        errorIndicatorColor = Color.Red
                    ),
                    isError = isnotmonth,
                    shape = RoundedCornerShape(20)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                OutlinedTextField(
                    value = Userday,
                    onValueChange = {
                        Userday = it
                        if(Userday.isNotEmpty()) {
                            isnotday = Userday.toInt() > 31 || Userday.toInt() < 0
                        }
                    },
                    modifier = Modifier
                        .weight(1f),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    placeholder = { Text("DD") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.DarkGray,
                        errorIndicatorColor = Color.Red
                    ),
                    isError = isnotday,
                    shape = RoundedCornerShape(20)
                )
            }
        }
    }
}