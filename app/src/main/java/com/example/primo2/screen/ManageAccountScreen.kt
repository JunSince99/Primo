package com.example.primo2.screen

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.*
import com.example.primo2.R
import com.example.primo2.ui.theme.Typography
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileBox(navController: NavController,requestManager: RequestManager, modifier: Modifier = Modifier) { // 미완성
    val selfPostArrayList = remember { mutableStateListOf<PostInfo>() }
        Box(
            modifier = Modifier,
        ) {
            Column {
                Spacer(modifier.size(25.dp))
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
                        if(partnerName != "") {
                            GlideImage(
                                model = partnerPhotoURL,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
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
                        }
                        else{
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
                            text = myName+"님",
                            modifier = Modifier
                                .padding(4.dp),
                            style = Typography.body1
                        )
                        if(partnerName != "") {
                            var today = Calendar.getInstance()
                            var compareTime = "error"
                            var sf = SimpleDateFormat("yyyy-MM-dd")
                            var date = sf.parse(startDating)
                            var calcDate = (today.time.time - date!!.time) / (60 * 1000 * 60 * 24)
                            val coupleText = partnerName+"님과 " + calcDate+"일 째 연애 중"
                            Text(
                                text = coupleText,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clickable {
                                    },
                                style = Typography.body1
                            )
                        }
                        else{
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
                    }

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

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .drawBehind {
            drawLine(
                Color.LightGray,
                Offset(0f, 0f),
                Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx()
            )
        })
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val contrast = 1f // 0f..10f (1 should be default)
            val brightness = -0f // -255f..255f (0 should be default)
            val colorMatrix = floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
            items((selfPostArrayList.size/4) + 1){
                Row()
                {
                    for(i in 0 until 4) {
                        if(it*4+i < selfPostArrayList.size)
                        {
                            val uri = selfPostArrayList[it * 4 + i].ImageResources[0]
                            GlideImage(
                                model = uri,
                                contentDescription = null,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix)),
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillParentMaxSize(0.25f)
                                    .clip(RectangleShape)
                                    .padding(2.dp)
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
