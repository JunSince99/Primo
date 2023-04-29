package com.example.primo2.screen

import PostViewModel
import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.DefaultStrokeLineWidth
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.*
import com.example.primo2.R
import com.example.primo2.ui.theme.Typography
import com.example.primo2.ui.theme.spoqasans
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.lang.Math.floor
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun ManageAccountScreen(
    onLogoutButton: () -> Unit = {},
    navController: NavController,
    requestManager: RequestManager,
    modifier: Modifier = Modifier
) {
    Surface()
    {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileBox(navController, requestManager)
            ProfilePosts(requestManager, Modifier, navController)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileBox(navController: NavController,requestManager: RequestManager, modifier: Modifier = Modifier) { // 미완성
    val selfPostArrayList = remember { mutableStateListOf<PostInfo>() }
    Surface(
        modifier = Modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(0,0,10,10)
            )
            .clip(RoundedCornerShape(0,0,10,10)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (partnerName != "") {
                GlideImage(
                    model = partnerPhotoURL,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
                {
                    it
                        .thumbnail(
                            requestManager
                                .asDrawable()
                                .load(partnerPhotoURL)
                                // .signature(signature)
                                .override(10)
                        )
                    // .signature(signature)
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier.size(5.dp))
            Text(
                text = myName + "님",
                modifier = Modifier
                    .padding(4.dp),
                style = Typography.body1
            )
            if (partnerName != "") {
                var today = Calendar.getInstance()
                var compareTime = "error"
                var sf = SimpleDateFormat("yyyy-MM-dd")
                var date = sf.parse(startDating)
                var calcDate = (today.time.time - date!!.time) / (60 * 1000 * 60 * 24)
                val coupleText = partnerName + "님과 " + calcDate + "일 째 연애 중"
                Text(
                    text = coupleText,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                        },
                    style = Typography.body1
                )
            } else {
                val coupleText = "커플 등록하기"
                Text(
                    text = coupleText,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            navController.navigate(PrimoScreen.RegisterPartner.name)
                        },
                    style = Typography.body1
                )
                val user = Firebase.auth.currentUser
                val clipboardManager: ClipboardManager = LocalClipboardManager.current
                val context = LocalContext.current
                Text(
                    text = "Code : " + user!!.uid,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            clipboardManager.setText(AnnotatedString(user.uid))
                            Toast
                                .makeText(
                                    context, "코드가 복사 됐습니다.",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    style = Typography.body1,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }

            val db = Firebase.firestore
            val user = Firebase.auth.currentUser
            var countPost by remember { mutableStateOf(0) }
            if (user != null) {
                db.collection("posts")
                    .whereEqualTo("writerID", user.uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        selfPostArrayList.clear()
                        for (document in documents) {
                            selfPostArrayList.add(
                                PostInfo(
                                    document.id,
                                    document.getString("title"),
                                    document.data["contents"] as ArrayList<String?>,
                                    document.data["splitNumber"] as ArrayList<Int>,
                                    document.data["imageResources"] as MutableList<String>,
                                    document.data["spam"] as ArrayList<Double>,
                                    document.data["background"] as ArrayList<Double>,
                                    document.data["person"] as ArrayList<Double>,
                                    document.data["placeName"] as ArrayList<String>,
                                    document.getString("writer"),
                                    document.getString("writerID"),
                                    document.getString("postDate"),
                                    document.data["like"] as HashMap<String, Boolean>
                                )
                            )
                        }
                        countPost = selfPostArrayList.size
                    }
            }
            Spacer(modifier = Modifier.size(20.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileInfoBlock(countPost.toString(), "게시물")
                ProfileInfoBlock("0", "팔로워")
                ProfileInfoBlock("0", "팔로잉")
            }
        }
    }
}

@Composable
fun ProfileInfoBlock(amount: String, info:String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = amount, style = Typography.body1)
        Text(text = info, style = Typography.body1)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProfilePosts(requestManager: RequestManager,
          modifier: Modifier = Modifier,
          navController: NavController
)
{
    val selfPostArrayList = remember { mutableStateListOf<PostInfo>() }
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    var countPost by remember { mutableStateOf(0) }
    if(user != null) {
        db.collection("posts")
            .whereEqualTo("writerID", user.uid)
            .get()
            .addOnSuccessListener { documents ->
                selfPostArrayList.clear()
                for(document in documents) {
                    selfPostArrayList.add(
                        PostInfo(
                            document.id,
                            document.getString("title"),
                            document.data["contents"] as ArrayList<String?>,
                            document.data["splitNumber"] as ArrayList<Int>,
                            document.data["imageResources"] as MutableList<String>,
                            document.data["spam"] as ArrayList<Double>,
                            document.data["background"] as ArrayList<Double>,
                            document.data["person"] as ArrayList<Double>,
                            document.data["placeName"] as ArrayList<String>,
                            document.getString("writer"),
                            document.getString("writerID"),
                            document.getString("postDate"),
                            document.data["like"] as HashMap<String, Boolean>
                        )
                    )
                }
                countPost = selfPostArrayList.size
            }
    }
    LazyColumn(modifier = modifier) {
        items(selfPostArrayList.size){
            ProfilePost(selfPostArrayList[it],requestManager,it, navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfilePost(postInfo: PostInfo,requestManager: RequestManager,num:Int,navController: NavController) {
    Surface(
        color = Color.White,
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
        ) {
            Box( //사진
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20))
                    .size(70.dp)
            ) {
                if (postInfo.Contents[0] != null)
                {
                    Box(modifier = Modifier
                    ) {
                        HorizontalPager(
                            modifier = Modifier,
                            count = postInfo.ImageResources.size
                        ) { page ->
                            val uri = postInfo.ImageResources[page]
                            // 이미지
                            GlideImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .aspectRatio(1f / 1f)
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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "송도",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontFamily = spoqasans,
                        fontWeight = FontWeight.Medium,
                    )
                    if (postInfo.title != null) {
                        Text(
                            text = postInfo.title,
                            color = Color.Black,
                            fontFamily = spoqasans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
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
                                text = "JuSiErW · ",
                                fontSize = 12.sp,
                                color = Color.DarkGray,
                                fontFamily = spoqasans,
                                fontWeight = FontWeight.Normal,
                            )
                            Text(
                                text = compareTime,
                                fontSize = 10.sp,
                                modifier = Modifier,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}