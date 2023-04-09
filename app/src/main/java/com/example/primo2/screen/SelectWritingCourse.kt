package com.example.primo2.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.R
import com.example.primo2.placeList
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans

@Composable
fun SelectCourse(navController:NavController, requestManager: RequestManager) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        var searchKeyword by remember { mutableStateOf("") }
        val searchPlaceList = remember { mutableStateListOf<Int>() }
        val postPlaceList = remember { mutableStateListOf<Int>() }
        LaunchedEffect(searchKeyword) {
            if (searchKeyword.isNotBlank()) {
                searchPlaceList.clear()
                for (i in 0 until placeList.size) {
                    if (placeList[i].placeName.contains(searchKeyword)) {
                        searchPlaceList.add(i)
                    }
                }
            }
        }
        Column {
            Defaulttopbar(navController,postPlaceList)
            SearchBar(searchKeyword, onSearchKeywordChange = {searchKeyword = it})
            Spacer(modifier = Modifier.size(8.dp))
            Course(searchKeyword,searchPlaceList,postPlaceList,requestManager, onSearchKeywordChange = {searchKeyword = it})
        }
    }
}

@Composable
fun Defaulttopbar(navController: NavController,postPlaceList: MutableList<Int>) {
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
                        .clickable {
                            navController.navigate("${PrimoScreen.WritingScreen.name}/$postPlaceList")
                        }
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
fun SearchBar(searchKeyword:String,onSearchKeywordChange:(String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        value = searchKeyword,
        onValueChange = { text ->
            onSearchKeywordChange(text)
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
                        onSearchKeywordChange("")
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
fun Course(searchKeyword: String,searchPlaceList: MutableList<Int>,postPlaceList: MutableList<Int>,requestManager:RequestManager, onSearchKeywordChange:(String) -> Unit) {
    if(searchKeyword.isNotBlank()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            items(searchPlaceList) { item ->
                SearchPlace(item,requestManager,postPlaceList,onSearchKeywordChange)
            }
        }
    }
    else {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(postPlaceList) { item ->
                PostPlace(item,requestManager)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostPlace(item:Int, requestManager: RequestManager)
{
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
            val url = placeList[item].imageResource
            GlideImage(
                model = url,
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp),
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
                modifier = Modifier,
            ) {
                Text(
                    text = placeList[item].placeName,
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
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchPlace(item:Int,requestManager: RequestManager,postPlaceList: MutableList<Int>,onSearchKeywordChange:(String) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val url = placeList[item].imageResource
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
        Row(modifier = Modifier
            .fillMaxSize()
            .clickable {
                postPlaceList.add(item)
                onSearchKeywordChange("")
                       }
        , horizontalArrangement = Arrangement.End){
            androidx.compose.material3.Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "포스트 장소 추가 버튼",
            )
        }
    }
}


