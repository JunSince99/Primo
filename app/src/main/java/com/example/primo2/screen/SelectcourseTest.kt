package com.example.primo2.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.placeList
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans

@Composable
fun SelectcourseTest() {
    Surface() {
        Column {
            DefaulttopbarTest()
            Spacer(modifier = Modifier.size(8.dp))
            PostPlaceTest()
            Surface(
                color = moreLightGray,
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "새로운 코스 추가하기 ",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Icon(
                        painter = painterResource(id = com.example.primo2.R.drawable.ic_baseline_add_circle_24),
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

@Composable
fun DefaulttopbarTest() {
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
                        .clickable {  }
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
fun PostPlaceTest()
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
            Image(
                painter = painterResource(id = com.example.primo2.R.drawable.place_centralpark),
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Column(
                modifier = Modifier,
            ) {
                Text(
                    text = "송도 센트럴파크 산책~",
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