package com.example.primo2.screen

import android.util.Log
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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.primo2.*
import com.example.primo2.R
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans

@Composable
fun SelectCourse(navController:NavController, requestManager: RequestManager,datePlanList: SnapshotStateList<DatePlanInfo>) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            Defaulttopbar(navController)
            Spacer(modifier = Modifier.size(8.dp))
            Course(datePlanList,requestManager,navController)
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
                            navController.navigate(PrimoScreen.WritingScreen.name)
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Course(datePlanList: SnapshotStateList<DatePlanInfo>,requestManager:RequestManager,navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(datePlanList.size) { item ->
            PostPlace(item,datePlanList,requestManager, navController)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostPlace(item:Int, datePlanList: SnapshotStateList<DatePlanInfo>, requestManager: RequestManager,navController: NavController)
{
    if(datePlanList[item].course.isNotEmpty()) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(20)
                )
                .aspectRatio(16f / 4f)
                .clickable {
                    postPlaceList.clear()
                    postPlaceList.addAll(datePlanList[item].course)
                    Log.e("",""+ datePlanList[item].course)
                    Log.e("",""+ postPlaceList)
                    navController.navigate(PrimoScreen.WritingScreen.name)
                }
        ) {
            Row(
                modifier = Modifier
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(6.dp))
                val url = placeListHashMap[datePlanList[item].course[0]]?.imageResource
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
                        text = datePlanList[item].dateTitle,
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
}