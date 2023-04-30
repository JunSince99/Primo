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
import androidx.compose.material.icons.filled.*
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun Postdetail(navController: NavController, item: Int,requestManager: RequestManager,viewModel: PostViewModel) {
    val uiState by viewModel.postState.collectAsState()
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
                        Posttitle(item, requestManager, postList, uiState[0]) //이거 수정좀 몇일전 게시글인지
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Postarticle(item, num, postList, requestManager)
                }
            }
        }
    }
}

@Composable
fun Posttopbar(navController: NavController) {
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
                        .clickable { navController.navigateUp() }
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
fun Posttitle(item: Int, requestManager:RequestManager,postList:ArrayList<PostInfo>, postInfo: PostInfo) {
    val contrast = 1f // 0f..10f (1 should be default)
    val brightness = -95f // -255f..255f (0 should be default)
    val colorMatrix = floatArrayOf(
        contrast, 0f, 0f, 0f, brightness,
        0f, contrast, 0f, 0f, brightness,
        0f, 0f, contrast, 0f, brightness,
        0f, 0f, 0f, 1f, 0f
    )
    Box {
        val uri = postList[item].ImageResources[0]
        // 이미지
        GlideImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f),
            colorFilter = ColorFilter.colorMatrix(colorMatrix = ColorMatrix(colorMatrix))
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
            TopAppBar(
                title = { Text("") },
                backgroundColor = Color.Transparent,
                navigationIcon = {
                    IconButton(onClick = {})
                    {
                        Icon(
                            Icons.Default.ArrowBack,
                            "",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            Icons.Default.Add,
                            "",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {  }) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            "",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {})
                    {
                        Icon(
                            Icons.Default.MoreVert,
                            "",
                            tint = Color.White
                        )
                    }
                },
                elevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(95.dp))
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (postInfo.PostDate != null) {

                    var today = Calendar.getInstance()
                    var compareTime = "error"
                    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    var date = sf.parse(postInfo.PostDate)
                    var calcuDate = (today.time.time - date.time) / (60 * 1000)
                    var timeUnit = "error"

                    if (calcuDate <= 0) {
                        compareTime = "방금 전"
                    } else {
                        timeUnit = "분 전"
                        compareTime = calcuDate.toString() + "개월 전"
                        if (calcuDate >= 60) {
                            calcuDate /= 60
                            timeUnit = "시간 전"
                            if (calcuDate >= 24) {
                                calcuDate /= 24
                                timeUnit = "일 전"
                                if (calcuDate >= 30) {
                                    calcuDate /= 30
                                    timeUnit = "개월 전"
                                    if (calcuDate >= 12) {
                                        calcuDate /= 12
                                        timeUnit = "년 전"
                                    }
                                }
                            }
                        }
                        compareTime = calcuDate.toString() + timeUnit
                    }
                    Text(
                        text = "JuSiErW · ",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = spoqasans,
                        fontWeight = FontWeight.Normal,
                    )
                    Text(
                        text = compareTime,
                        fontSize = 12.sp,
                        modifier = Modifier,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun Postarticle(item: Int, num: Int, postList: ArrayList<PostInfo>,requestManager: RequestManager) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(10)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                //장소
                val uri = placeListHashMap[postList[item].placeName[num]]!!.imageResource
                GlideImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(10)),
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

            Spacer(modifier = Modifier.padding(6.dp))
            //내용
            Text(
                text = postList[item].Contents[num]!!,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 16.sp,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}