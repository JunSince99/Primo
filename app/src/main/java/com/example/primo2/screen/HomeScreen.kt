package com.example.primo2.screen

import PostViewModel
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.primo2.getWriterInfomation
import com.example.primo2.ui.theme.LazyColumnExampleTheme
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
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date


@Composable
fun HomeScreen(
    onUploadButtonClicked: () -> Unit = {},
    navController: NavController,
    requestManager: RequestManager,
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel()
){
        val user = Firebase.auth.currentUser
        if(user == null) {
            navController.navigate(PrimoScreen.Login.name)
        }
        LazyColumnExampleTheme() {
            Surface(
                modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
                color = MaterialTheme.colors.onBackground // app.build.gradle에서 색 지정 가능
            ) {
                Scaffold() { padding ->
                    Posts(requestManager, Modifier.padding(padding), viewModel)
                }
            }
        }
}

// 게시글들을 띄우는 함수
@Composable
fun Posts(requestManager: RequestManager,
          modifier: Modifier = Modifier,
          viewModel: PostViewModel = viewModel()
)
{
    Log.e("호출","인데")
    val uiState by viewModel.postState.collectAsState()
    if(uiState.isEmpty())
    {
        if(!viewModel.isUpdate) {
            viewModel.updatePostInformation()
            viewModel.isUpdate = true
        }
    }
    LazyColumn(modifier = modifier) {
        items(uiState.size){
            Post(uiState[it],requestManager,it)
        }
    }

}
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun Post(postInfo: PostInfo,requestManager: RequestManager,num:Int) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20)
            )
            .aspectRatio(16f / 10f)
            .clickable { /*TODO*/ }
    ) {
        if (postInfo.Contents[0] != null) // 사진 & 동영상
        {
            Box(modifier = Modifier //박스로 안감싸주면 이미지,동영상 크기에 따라 들쭉날쭉거려서 박스로 감싸줬음 아마 제목,내용 이런것도 다 박스로 감싸서 정규화시켜줘야할듯
                .fillMaxWidth()
            ) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = postInfo.Contents.size
                ) { page ->
                    val uri = postInfo.Contents[page]
                    val format = postInfo.Format[page]
                    if (format.equals("video")) { // 동영상
                        val mContext = LocalContext.current
                        val mediaItem = MediaItem.Builder().setUri(Uri.parse(uri)).build()
                        val mExoPlayer = remember(mContext) {
                            ExoPlayer.Builder(mContext).build().apply {
                                this.setMediaItem(mediaItem)
                                playWhenReady = true
                                prepare()
                            }
                        }
                        AndroidView(factory = { context ->
                            StyledPlayerView(context).apply {
                                player = mExoPlayer
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                            }
                        })
                    } else {  // 이미지
                        GlideImage(
                            model = uri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                            //.align(Alignment.CenterHorizontally)
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
                    }
                }
            }

        }
        Column(
            modifier = Modifier
                .background(brush = SolidColor(Color.Black), alpha = 0.3f) // 이미지 어둡게
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            if (postInfo.title != null) {
                Text(
                    text = postInfo.title,
                    color =Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Log.e("",""+postInfo.title)
            }
            Text(
                text = "걷기 좋은 공원 | "+postInfo.Writer+"님",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1.5f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier
                ) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    var likeCount by remember { mutableStateOf(0) }
                    likeCount = postInfo.Like.count()
                    Row(modifier = Modifier){
                        var likeColor:Color = Color.White
                        var iconImage:ImageVector = Icons.Filled.FavoriteBorder
                        if(postInfo.Like.containsKey(uid))
                        {
                            likeColor = Color.Red
                            iconImage = Icons.Filled.Favorite
                        }

                        Icon(
                            imageVector = iconImage,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    if(postInfo.Like.containsKey(uid)){
                                        postInfo.Like.remove(uid)
                                    }
                                    else{
                                        postInfo.Like[uid!!] = true
                                    }
                                    likeCount = postInfo.Like.count()
                                    savePostLike(postInfo.Like,postInfo.postID!!)
                                }
                                .padding(horizontal = 2.dp),
                            tint = likeColor
                        )
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { /*TODO*/ }
                                .padding(horizontal = 2.dp),
                            tint = Color.White
                        )
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { /*TODO*/ }
                                .padding(horizontal = 2.dp),
                            tint = Color.White
                        )
                    }
                    Text(
                        color =Color.White,
                        text = "좋아요" + likeCount + "개",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                if(postInfo.PostDate != null) {

                    var today = Calendar.getInstance()
                    var compareTime = "error"
                    var sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    var date = sf.parse(postInfo.PostDate)
                    var calcuDate = (today.time.time - date.time) / (60 * 1000)
                    var timeUnit = "error"

                    if(calcuDate <= 0)
                    {
                        compareTime = "방금 전"
                    }
                    else
                    {
                        timeUnit = "분 전"
                        compareTime = calcuDate.toString() + "개월 전"
                        if(calcuDate >= 60)
                        {
                            calcuDate/=60
                            timeUnit = "시간 전"
                        }
                        if(calcuDate >= 24)
                        {
                            calcuDate/=24
                            timeUnit = "일 전"
                        }
                        if(calcuDate >= 30)
                        {
                            calcuDate/=30
                            timeUnit = "개월 전"
                        }
                        if(calcuDate >= 12)
                        {
                            calcuDate/=12
                            timeUnit = "년 전"
                        }
                        compareTime = calcuDate.toString() + timeUnit
                    }

                    Text(
                        text = compareTime,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Bottom)
                            .padding(horizontal = 4.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

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