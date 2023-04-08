package com.example.primo2.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.primo2.R
import com.example.primo2.ui.theme.spoqasans

@Composable
fun Post() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //탑바
            Posttopbar()
            //이미지 추가 버튼
            //제목
            Posttitle()
            Spacer(modifier = Modifier.size(8.dp))
            Postarticle()
        }
    }
}

@Composable
fun Posttopbar() {
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
            }
        }
    }
}

@Composable
fun Posttitle() {
    Text(
        text = "전통과 함께하는 전주 한옥마을",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        fontSize = 25.sp,
        fontFamily = spoqasans,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Postarticle() {
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
            Spacer(modifier = Modifier.size(16.dp))
            Image(
                painter = painterResource(id = R.drawable.place_centralpark),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .aspectRatio(16f / 10f)
                    .clip(shape = RoundedCornerShape(10))
                    .fillMaxWidth()
            )
            //내용
            Text(
                text = "안녕하세요 오늘은 한옥마을에 가는 법에 대해서\n알아볼게요. 한옥마을은 정말 아름답기로 유명한데요\n지금까지 한옥마을 가는 법에 대해서 알아봤어요!",
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