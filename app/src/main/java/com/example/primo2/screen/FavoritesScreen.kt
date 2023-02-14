package com.example.primo2.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.*
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    requestManager:RequestManager,
    modifier: Modifier = Modifier
) {
    val sortedMap = userOrientation.toList().sortedByDescending { it.second.toString().toDouble() }.toMap() as MutableMap
    val num = 5 // 추천 할 태그의 개수

    if(tasteName.isEmpty()){
        getTasteName()
    }
    Column(modifier = Modifier) {
        Surface(
            modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
            color = MaterialTheme.colors.onBackground // app.build.gradle에서 색 지정 가능
        ) {
            Scaffold() { padding ->
                RecommendPlaces(requestManager, Modifier.padding(padding), sortedMap.toList(), num)
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RecommendPlaces(requestManager: RequestManager,
              modifier: Modifier = Modifier,
              sortedMap: List<Pair<String,Any>>,
                    num:Int
)
{
    LazyColumn(modifier = modifier) {
        items(num){
            RecommendPlace(requestManager,sortedMap[it].first,5)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecommendPlace(requestManager: RequestManager,name:String,placeNum:Int) {
    placeList.sortByDescending { it.placeHashMap?.get(name)?.toString()?.toDouble() }
    Text(
        text = tasteName.get(name).toString() + " 데이트 코스",
        color =Color.Black,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
    )
    val imageList:ArrayList<String> = ArrayList()
    for(i in 0 until placeNum) {
        imageList.add(placeList[i].imageResource)
        Log.e(""+name,""+ placeList[i].placeName )
    }
    LazyRow {
        items(placeNum){ index ->
            val url = imageList[index]
            Column() {
                GlideImage(
                    model = url ,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                    //.align(Alignment.CenterHorizontally)
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
                    // .signature(signature)
                }
            }
        }
    }

}
