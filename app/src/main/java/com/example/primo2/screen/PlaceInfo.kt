package com.example.primo2.screen
/*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.primo2.ui.theme.moreLightGray

@Composable
fun info() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp)
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

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp),
                        tint = Color.Black
                    )
                }
            }
            Image(
                painter = painterResource(id = com.example.primo2.R.drawable.place_centralpark),
                contentDescription = "sample image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .aspectRatio(16f / 10f)
                    .clip(shape = RoundedCornerShape(10))
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "센트럴 파크",
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Row {
                placetag("걷기 좋은")
                placetag("공원")
                placetag("전통")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row{
                buttonwithicon(ic = Icons.Outlined.FavoriteBorder, description = "저장하기")
                Spacer(modifier = Modifier.padding(12.dp))
                buttonwithicon(ic = painterResource(id = com.example.primo2.R.drawable.ic_outline_comment_24), description = "리뷰보기")
                Spacer(modifier = Modifier.padding(12.dp))
                buttonwithicon(ic = painterResource(id = com.example.primo2.R.drawable.ic_outline_calendar_month_24), description = "일정추가")
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                color = moreLightGray,
                thickness = 2.dp
            )

            Surface( //지도
                shape = RoundedCornerShape(10.dp),
                color = Color.Black,
                modifier = Modifier
                    .aspectRatio(16f / 10f)
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "지 도",
                    fontSize = 100.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                information(title = "주소", text = "인천 연수구 컨벤시아대로 160")
                information("이용 가능 시간", "연중 무휴")
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                color = moreLightGray,
                thickness = 2.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "리뷰",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                reviewform(name = "정석준", score = 4, text = "산책하기 정말 좋은 공원이에요")
                reviewform(name = "삼삼삼", score = 4, text = "삼삼삼삼삼삼삼삼삼")
            }
        }
    }
}

@Composable
fun buttonwithicon(ic : ImageVector, description : String){
    Column(
        modifier = Modifier
            .clickable { }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ic,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        Text(
            text = description,
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun buttonwithicon(ic : Painter, description: String){
    Column(
        modifier = Modifier
            .clickable { }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = ic,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        Text(
            text = description,
            color = Color.DarkGray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun information(title: String, text : String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
fun reviewform(name : String, score : Int, text: String) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = com.example.primo2.R.drawable.dog),
                contentDescription = "sample image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(43.dp)
                    .clip(shape = CircleShape)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                Text(
                    text = name
                )
                Row{
                    Text(
                        text = "리뷰 10개 "
                    )
                    Text(
                        text = "별점평균 5.0 | "
                    )
                    Text(
                        text = "2023.03.01"
                    )
                }
            }
        }
        Text(
            text = text
        )
    }
}

 */