package com.example.primo2.screen

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.DefaultStrokeLineWidth
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primo2.R
import com.example.primo2.ui.theme.Typography
import com.naver.maps.map.compose.GroundOverlayDefaults.Image
import kotlin.text.Typography

@Composable
fun ManageAccountScreen(
    onLogoutButton: () -> Unit = {},
    naviController: NavController,
    modifier: Modifier = Modifier
) {
    Surface()
    {
        ProfileBox()
    }
}

@Composable
fun ProfileBox() { // 미완성
        Box(
            modifier = Modifier,
        ) {
            Column {
                Text( // top bar로 만들어야할듯?
                    text = "프로필",
                    style = Typography.h5.copy(fontSize = 36.sp),
                    modifier = Modifier
                        .padding(12.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dog),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = "JuSiErW님",
                            modifier = Modifier
                                .padding(4.dp),
                            style = Typography.body1
                        )
                        Text(
                            text = "aa님과 00일 째 연애 중",
                            modifier = Modifier
                                .padding(4.dp),
                            style = Typography.body1
                        )
                    }

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileInfoBlock("3", "게시물")
                        ProfileInfoBlock("0", "팔로워")
                        ProfileInfoBlock("0", "팔로잉")
                    }
                }
            }
        }
}

@Composable
fun ProfileInfoBlock(amount: String, info:String) {
    val size = 20.sp
    Column(
        modifier = Modifier
            .padding(horizontal = 28.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = amount, style = Typography.body1)
        Text(text = info, style = Typography.body1)
    }
}
