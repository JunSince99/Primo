package com.example.primo2.screen

import PostViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.primo2.ui.theme.White
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalNaverMapApi::class)
@Composable
fun Placedetail(navController: NavController, item: Int,requestManager: RequestManager) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        if(entireDatePlanName.isNullOrEmpty()){
            navController.navigate(PrimoScreen.Home.name)
        }
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
            val uri = placeList[item].imageResource
            GlideImage(
                model = uri,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .aspectRatio(16f / 10f)
                    .clip(shape = RoundedCornerShape(10))
                    .fillMaxWidth()
            )
            {
                it
                    .thumbnail(
                        requestManager
                            .asDrawable()
                            .load(uri)
                            // .signature(signature)
                            .override(128)
                    )
                // .signature(signature)
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = placeList[item].placeName,
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Row {
                for(i in 0 until placeList[item].toptag.size) {
                    placetag(placeList[item].toptag[i], 10.sp)
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                buttonwithicon(ic = Icons.Outlined.FavoriteBorder, description = "저장하기")
                Spacer(modifier = Modifier.padding(12.dp))
                buttonwithicon(
                    ic = painterResource(id = R.drawable.ic_outline_comment_24),
                    description = "리뷰보기"
                )
                val context = LocalContext.current
                Spacer(modifier = Modifier.padding(12.dp))
                    buttonwithicon(
                        ic = painterResource(id = R.drawable.ic_outline_calendar_month_24),
                        description = "일정추가",
                        onButtonClicked = {
                            val database = Firebase.database.reference
                                .child("DatePlan")
                                .child(leaderUID)
                            val courseList:ArrayList<String> = arrayListOf()
                            database.child(entireDatePlanName!!).child("course").get().addOnSuccessListener {
                                for(i in 0 until it.childrenCount)
                                {
                                    courseList.add(it.child(i.toString()).value.toString())
                                }
                                val ID = placeList[item].placeID
                                if (courseList.indexOf(ID) == -1) {
                                    courseList.add(ID)

                                    database
                                        .child(entireDatePlanName!!)
                                        .child("course")
                                        .setValue(courseList)
                                        .addOnSuccessListener {
                                            Toast
                                                .makeText(context, "일정에 장소를 추가 했습니다", Toast.LENGTH_SHORT)
                                                .show();
                                        }

                                } else {
                                    Toast
                                        .makeText(context, "이미 추가된 장소입니다.", Toast.LENGTH_SHORT)
                                        .show();
                                }
                            }
                        }
                    )
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

                var mapProperties by remember {
                    mutableStateOf(
                        MapProperties(maxZoom = 20.0, minZoom = 5.0)
                    )
                }
                val mapUiSettings by remember {
                    mutableStateOf(
                        MapUiSettings(isLocationButtonEnabled = false)
                    )
                }
                val placePosition = LatLng(placeList[item].latitude, placeList[item].longitude)
                val cameraPositionState: CameraPositionState = rememberCameraPositionState {
                    // 카메라 초기 위치를 설정합니다.
                    position = CameraPosition(placePosition, 15.0)
                }
                Box(Modifier.fillMaxSize()) {
                    NaverMap(properties = mapProperties, uiSettings = mapUiSettings, cameraPositionState = cameraPositionState)
                    {
                        Marker(
                            icon = MarkerIcons.BLACK/*OverlayImage.fromResource(R.drawable.circle)*/,
                            width = 20.dp,
                            height = 20.dp,
                            state = MarkerState(
                                position = LatLng(
                                    placeList[item].latitude,
                                    placeList[item].longitude
                                )
                            )
                            //captionText = placeList[i].placeName + "\n" + (courseIndex + 1),
                            //captionColor = Color.Green,
                        )
                    }
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                information(title = "주소", text = placeList[item].address)
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
fun buttonwithicon(ic : ImageVector, description : String, onButtonClicked: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .clickable { onButtonClicked() }
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
fun buttonwithicon(ic : Painter, description: String, onButtonClicked: () -> Unit = {}){
    Column(
        modifier = Modifier
            .clickable { onButtonClicked() }
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
                painter = painterResource(id = R.drawable.dog),
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
