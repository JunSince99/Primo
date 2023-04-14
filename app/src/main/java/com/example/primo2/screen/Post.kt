package com.example.primo2.screen

import PostViewModel
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
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
import com.example.primo2.PostInfo
import com.example.primo2.R
import com.example.primo2.placeListHashMap
import com.example.primo2.postPlaceList
import com.example.primo2.ui.theme.spoqasans
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Composable
fun Postdetail(navController: NavController, item: Int,requestManager: RequestManager,viewModel: PostViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val postList = viewModel.postList2
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Spacer(modifier = Modifier.size(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(postList[item].Contents.size) { num ->
                    if(num == 0){
                        Posttitle(item, requestManager, postList)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Postarticle(item, num, postList, requestManager)
                }
            }
        }
    }
    TopAppBar(
        title = { Text("") },
        backgroundColor = Color.Transparent,
        navigationIcon = {
            IconButton(onClick = {})
            {
                Icon(
                    Icons.Default.ArrowBack,
                    "Open/Close menu",
                    tint = Color.White
                )
            }
        },
        elevation = 1.dp
    )
}

@Composable
fun Posttopbar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
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
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Posttitle(item: Int, requestManager:RequestManager,postList:ArrayList<PostInfo>) {
    Box {
        val uri = postList[item].ImageResources[0]
        // 이미지
        GlideImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
            //.align(Alignment.CenterHorizontally)
        )
        {
            it
                .thumbnail(
                    requestManager
                        .asDrawable()
                        .load(uri)
                        // .signature(signature)
                        .override(64)
                )
            // .signature(signature)
        }

        Column {
            Spacer(modifier = Modifier.size(140.dp))
            Text(
                text = "송도",
                fontSize = 17.5.sp,
                color = Color.White,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.padding(1.dp))
            Text(
                text = postList[item].title!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                fontSize = 25.sp,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "걷기 좋은 공원",
                fontSize = 17.5.sp,
                color = Color.White,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun Postarticle(item: Int, num: Int, postList: ArrayList<PostInfo>,requestManager: RequestManager) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(10)
            )
    ) {
        Column {
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                //장소
                Image(
                    painter = painterResource(id = R.drawable.dog),
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
                    val placeName = placeListHashMap[postList[item].placeName[num]]!!.placeName
                    Text(
                        text = placeName,
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
            Spacer(modifier = Modifier.size(16.dp))
            var start = 0
            var count = 0
            if(num == 0){
                count = postList[item].SplitNumber[num]
            } else{
                start = postList[item].SplitNumber[num - 1]
                count = postList[item].SplitNumber[num] - postList[item].SplitNumber[num - 1]
            }
            if(count > 0){
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = count
                ) { page ->
                    val uri = postList[item].ImageResources[start + page]
                    GlideImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 10f)
                            .clip(shape = RoundedCornerShape(10))
                            .fillMaxWidth()
                        //.align(Alignment.CenterHorizontally)
                    )
                    {
                        it
                            .thumbnail(
                                requestManager
                                    .asDrawable()
                                    .load(uri)
                                    // .signature(signature)
                                    .override(64)
                            )
                        // .signature(signature)
                    }
                }
            }




            //내용
            Text(
                text = postList[item].Contents[num]!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 16.sp,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}