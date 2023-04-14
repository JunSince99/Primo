package com.example.primo2.screen

import PostViewModel
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.MemberInfo
import com.example.primo2.PostInfo
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography
import com.example.primo2.ui.theme.spoqasans
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.text.webvtt.WebvttCssStyle.FontSizeUnit
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import kotlin.text.Typography


private data class ScrollParams(
    val index: Int,
    val scrollOffset: Int
)


@Composable
fun HomeScreen(
    onUploadButtonClicked: () -> Unit = {},
    navController: NavController,
    requestManager: RequestManager,
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel(),
    listState: LazyListState = LazyListState()
){

    val user = Firebase.auth.currentUser
    if(user == null) {
        navController.navigate(PrimoScreen.Login.name)
    }
    else {
            LaunchedEffect(true) {
                val db = Firebase.firestore
                val docRef = db.collection("users").document(user!!.uid)
                docRef.get()// 유저 정보 불러오기
                    .addOnSuccessListener { document ->
                        if (!document.exists()) {
                            navController.navigate(PrimoScreen.MemberInit.name)
                        }
                    }
            }
        }
    LazyColumnExampleTheme() {
        Surface(
            modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
            color = MaterialTheme.colors.onBackground // app.build.gradle에서 색 지정 가능
        ) {
            Scaffold() { padding ->
                Posts(requestManager, Modifier.padding(padding), viewModel,listState)
            }
        }
    }
}

// 게시글들을 띄우는 함수
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Posts(requestManager: RequestManager,
          modifier: Modifier = Modifier,
          viewModel: PostViewModel = viewModel(),
          listState: LazyListState = LazyListState()
)
{
    val uiState by viewModel.postState.collectAsState()
    if(uiState.isEmpty())
    {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
        }
        if(!viewModel.isUpdate) {
            viewModel.updatePostInformation()
            viewModel.isUpdate = true
        }
    }

    LazyColumn(modifier = modifier, state = listState) {
        items(uiState.size){
            Post(uiState[it],requestManager,it)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        listState.scrollToItem(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
    }
}
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun Post(postInfo: PostInfo,requestManager: RequestManager,num:Int) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .clickable { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(15.dp))

            ) {
                if (postInfo.Contents[0] != null) // 사진 & 동영상
                {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        val contrast = 1f // 0f..10f (1 should be default)
                        val brightness = -0f // -255f..255f (0 should be default)
                        val colorMatrix = floatArrayOf(
                            contrast, 0f, 0f, 0f, brightness,
                            0f, contrast, 0f, 0f, brightness,
                            0f, 0f, contrast, 0f, brightness,
                            0f, 0f, 0f, 1f, 0f
                        )
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize(),
                            count = postInfo.ImageResources.size
                        ) { page ->
                            val uri = postInfo.ImageResources[page]
                              // 이미지
                            GlideImage(
                                model = uri,
                                contentDescription = null,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix)),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RectangleShape)
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
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "송도",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.padding(1.dp))
                if (postInfo.title != null) {
                    Text(
                        text = postInfo.title,
                        color = Color.Black,
                        style = Typography.h2,
                        modifier = Modifier
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
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
                            text = "걷기 좋은 공원",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            fontFamily = spoqasans,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            text = compareTime,
                            fontSize = 12.sp,
                            modifier = Modifier,
                            color = Color.DarkGray
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

//Box( 아이콘 형식
//contentAlignment = Alignment.Center,
//modifier = Modifier
//.size(28.dp)
//.clip(CircleShape)
//.clickable { /*TODO*/ }
//) {
//    Icon(
//        imageVector = Icons.Outlined.FavoriteBorder,
//        contentDescription = null,
//        modifier = Modifier
//            .size(18.dp),
//        tint = Color.Black
//    )


//                Spacer(modifier = Modifier.padding(horizontal = 90.dp))
//                val uid = FirebaseAuth.getInstance().currentUser?.uid
//                var likeCount by remember { mutableStateOf(0) }
//                likeCount = postInfo.Like.count()
//                Row(modifier = Modifier){
//                    var likeColor:Color = Color.DarkGray
//                    var iconImage:ImageVector = Icons.Filled.FavoriteBorder
//                    if(postInfo.Like.containsKey(uid))
//                    {
//                        likeColor = Color.Red
//                        iconImage = Icons.Filled.Favorite
//                    }

//                    Icon(
//                        imageVector = iconImage,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clip(CircleShape)
//                            .clickable {
//                                if (postInfo.Like.containsKey(uid)) {
//                                    postInfo.Like.remove(uid)
//                                } else {
//                                    postInfo.Like[uid!!] = true
//                                }
//                                likeCount = postInfo.Like.count()
//                                savePostLike(postInfo.Like, postInfo.postID!!)
//                            }
//                            .padding(horizontal = 4.dp)
//                            .size(14.dp),
//                        tint = likeColor
//                    )
//                    Icon(
//                        imageVector = Icons.Outlined.Share,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clip(CircleShape)
//                            .clickable { /*TODO*/ }
//                            .padding(horizontal = 4.dp)
//                            .size(14.dp),
//                        tint = Color.DarkGray
//                    )
//                    Text(
//                        color =Color.Black,
//                        text = "좋아요" + likeCount + "개",
//                        fontSize = 14.sp,
//                        modifier = Modifier.padding(horizontal = 4.dp)
//                    )

fun savePostLike(likeInfo: HashMap<String,Boolean>,documentID: String)
{
    val db = Firebase.firestore
    val docRef = db.collection("posts").document(documentID)

    db.runTransaction { transaction ->
        val snapshot = transaction.get(docRef)
        transaction.update(docRef, "like", likeInfo)
        likeInfo
    }
}