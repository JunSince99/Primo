package com.example.primo2.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.primo2.R
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans

@Composable
fun SelectCourse(navController:NavController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Defaulttopbar(navController)
            SearchBar()
            Spacer(modifier = Modifier.size(8.dp))
            Course()
        }
    }
}

@Composable
fun Defaulttopbar(navController: NavController) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .clickable { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = "코스 선택",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { navController.navigate(PrimoScreen.WritingScreen.name) }
                ) {
                    Text(
                        text = "건너뛰기",
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontFamily = spoqasans,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    var searchKeyword by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .padding(horizontal = 24.dp)
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
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Black
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = moreLightGray,
            cursorColor = Color.Black,
            focusedIndicatorColor = moreLightGray,
            unfocusedIndicatorColor = moreLightGray
        )
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Course() {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(20)
            )
            .aspectRatio(16f / 4f)
            .clickable {}
    ) {
        Row(
            modifier = Modifier
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(6.dp))
            Image(
                painter = painterResource(id = R.drawable.place_centralpark),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = "센트럴 파크",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "인천광역시 송도동",
                    color = Color.DarkGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
            }
        }
    }
}