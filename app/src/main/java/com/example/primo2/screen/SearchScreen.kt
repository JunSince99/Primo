package com.example.primo2.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primo2.ui.theme.moreLightGray

@Composable
fun SearchScreen(
    naviController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold()
    {
        modifier.padding(it)
        Text(text = "서치 페이지", style = MaterialTheme.typography.h4)

    }
}

@Composable
fun Search() {
    var searchKeyword by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = searchKeyword,
                onValueChange = { text ->
                    searchKeyword = text
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "검색",
                        color = Color.Gray
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = {/*뒤로가기 만들어줘!*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = {
                            if (searchKeyword.isNotEmpty()) {
                                searchKeyword = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = Color.Black
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = moreLightGray,
                    unfocusedIndicatorColor = moreLightGray
                )
            )
            place()
            place()
            place()
            place()

        }
    }
}

@Composable
fun place(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = com.example.primo2.R.drawable.dog),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Text(
                text = "장소 이름",
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Row {
                placetag("걷기 좋은", 10.sp)
                placetag("공원", 10.sp)
                placetag("전통", 10.sp)
            }
        }
    }
}

@Composable
fun placetag(tagname : String, scale : TextUnit){
    Box(
        modifier = Modifier
            .border(
                1.dp,
                Color.LightGray,
                RoundedCornerShape(60)
            )
    ) {
        Text(
            text = tagname,
            color = Color.Black,
            fontSize = scale,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(6.dp)
        )
    }
    Spacer(modifier = Modifier.padding(4.dp))
}