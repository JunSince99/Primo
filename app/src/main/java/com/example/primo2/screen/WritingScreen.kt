package com.example.primo2.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.PostInfo
import com.example.primo2.R
import com.example.primo2.placeList
import com.example.primo2.postPlaceList
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WritingScreen(navController: NavController,requestManager: RequestManager) {
    var sequence by remember { mutableStateOf(0) }
    var article by remember { mutableStateOf("") }
    var titlename by remember { mutableStateOf("") }
    val titleList = arrayListOf<String>()
    val articleList = arrayListOf<String>()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //탑바
            Writingtopbar(sequence,postPlaceList,navController, onSequenceChange = {sequence = it}, article, titlename)
            Spacer(modifier = Modifier.padding(16.dp))
            //이미지 추가 버튼
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.LightGray,
                    contentColor = Color.Black
                ),
                modifier = Modifier.size(width = 100.dp, height = 100.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp),
                )
            }
            //제목

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = titlename,
                onValueChange = { text ->
                    titlename = text
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "제목을 입력해주세요.",
                        color = Color.Gray,
                        fontSize = 25.sp,
                        fontFamily = spoqasans,
                        fontWeight = FontWeight.Bold
                    )
                },
                textStyle = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Bold
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )
            placearticle(sequence,requestManager,article,onArticleChange = {article = it})
        }
    }
}

@Composable
fun Writingtopbar(sequence: Int, postPlaceList:MutableList<Int>,navController: NavController,onSequenceChange:(Int) -> Unit,article: String,titlename:String,articleList:ArrayList<String>,titleList:ArrayList<String>) {
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
                    text = "게시글 작성",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            if (postPlaceList.size - 1 == sequence || postPlaceList.size == 0) {
                                val firebaseFirestore = Firebase.firestore
                                val documentReference = firebaseFirestore
                                    .collection("posts")
                                    .document()

                                val postInfo =
                                    PostInfo(
                                        documentReference.id,
                                        title,
                                        contentsList,
                                        formatList,
                                        comments,
                                        user!!.uid,
                                        LocalDate
                                            .now()
                                            .format(
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                            )
                                    )

                                navController.navigate(PrimoScreen.Home.name)
                            } else {
                                titleList.add(titlename)
                                articleList.add(article)
                                onSequenceChange(sequence + 1)
                                //다음 페이지로
                            }
                        }
                ) {
                    val nextorend = if(postPlaceList.size - 1 == sequence || postPlaceList.size == 0) {
                        "완료"
                    } else{
                        "다음"
                    }
                    Log.e("",""+postPlaceList.size+" " + sequence)
                        Text(
                            text = nextorend,
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
fun placearticle(sequence: Int,requestManager:RequestManager,article:String,onArticleChange:(String) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(10)
            )
            .aspectRatio(16f / 16f)
    ) {
        Column {
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //장소
                val url = placeList[sequence].imageResource
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
                    var placeName = ""
                    if(postPlaceList.size != 0)
                    {
                        placeName = placeList[postPlaceList[sequence]].placeName
                    }
                    Text(
                        text = placeName,
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
            //내용

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = article,
                onValueChange = { text ->
                    onArticleChange(text)
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "내용을 입력해주세요.",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontFamily = spoqasans,
                        fontWeight = FontWeight.Medium
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Medium
                ),
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )
        }
    }
}