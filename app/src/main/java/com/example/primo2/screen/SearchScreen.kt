package com.example.primo2.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.placeList
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans
import com.example.primo2.userOrientation
import kotlin.math.absoluteValue

@Composable
fun Search(requestManager: RequestManager,navController: NavController) {
    var searchKeyword by remember { mutableStateOf("") }
    val searchPlaceList = remember { mutableStateListOf<Int>() }
    LaunchedEffect(searchKeyword)
    {
        if(searchKeyword.isBlank())
        {
            searchPlaceList.clear()
        }
        if (searchKeyword.isNotBlank()) {
            searchPlaceList.clear()
            for (i in 0 until placeList.size) {
                if (placeList[i].placeName.contains(searchKeyword)) {
                    searchPlaceList.add(i)
                }
            }
        }
    }
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
                        onClick = { navController.navigateUp() }
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
            if(searchKeyword.isEmpty()) {
                Places(requestManager, navController)
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    items(searchPlaceList) { item ->
                        Place(item, requestManager, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Places(requestManager: RequestManager,navController: NavController){
    Spacer(modifier = Modifier.padding(8.dp))
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = "추천 장소",
            fontSize = 20.sp,
            fontFamily = spoqasans,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(4.dp))

        //선호도순 정렬
        placeList.sortBy {
            var total = 0
            for((key,value) in userOrientation){
                if(key != "편의시설" && key != "대중교통") {
                    if (it.placeHashMap!!.containsKey(key)) {
                        total += (value-it.placeHashMap[key].toString().toInt()).absoluteValue
                    }
                }
            }
            total
        }
        for(i in 0 until 3) {
            Place(i, requestManager, navController)
        }
        Button(
            onClick = { /*TODO*/ },
            elevation = ButtonDefaults.elevation(
                defaultElevation = 1.dp,
                pressedElevation = 0.dp
            ),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "더보기",
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Place(item:Int,requestManager: RequestManager,navController: NavController){
    Row {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                .clickable {
                    navController.navigate("${PrimoScreen.PlaceDetailScreen.name}/$item")
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                val url = placeList[item].imageResource
                Row {
                    GlideImage(
                        model = url,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp),
                        contentScale = ContentScale.Crop

                    )
                    {
                        it
                            .thumbnail(
                                requestManager
                                    .asDrawable()
                                    .load(url)
                                    // .signature(signature)
                                    .override(64)
                            )
                    }
                    Spacer(modifier = Modifier.padding(6.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                    ) {
                        Text(
                            text = placeList[item].placeName,
                            color = Color.Black,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row {
                            for (i in 0 until 4) {
                                placetag(placeList[item].toptag[i], 8.sp)
                            }
                        }
                    }
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(width = 50.dp,height = 30.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = moreLightGray,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(1.dp)
                ) {
                    Text(
                        text = "추가",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                    )
                }
            }
        }
//        Button(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .weight(0.7f)
//                .aspectRatio(1f / 1f),
//            elevation = ButtonDefaults.elevation(
//                defaultElevation = 1.dp,
//                pressedElevation = 0.dp
//            ),
//            shape = RoundedCornerShape(20.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color.White,
//                contentColor = Color.Black
//            )
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = "장소 추가",
//                modifier = Modifier.size(20.dp)
//            )
//        }
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