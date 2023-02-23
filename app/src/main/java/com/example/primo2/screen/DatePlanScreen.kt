package com.example.primo2.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePlanScreen(
    navController: NavController,
    requestManager:RequestManager,
    listState: LazyListState = LazyListState(),
    datePlanList: SnapshotStateList<DatePlanInfo>,
    modifier: Modifier = Modifier
) {
    var imageUrlList:List<String> = listOf()
//Firebase.database.reference.child("DatePlan").child(user!!.uid)
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    var leaderUID = ""
    db.collection("users").document(user!!.uid)
        .get()
        .addOnSuccessListener { document ->
            leaderUID = document.getString("leaderUID") as String
            val database = Firebase.database.reference.child("DatePlan").child(leaderUID)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    datePlanList.clear()
                    imageUrlList = listOf()
                    for (datePlanSnapshot in dataSnapshot.children) {
                        val title = datePlanSnapshot.child("dateTitle").value.toString()
                        val startDate = datePlanSnapshot.child("startDate").value.toString()
                        val endDate = datePlanSnapshot.child("endDate").value.toString()
                        val course: MutableList<String> = mutableListOf()
                        val courseCount = datePlanSnapshot.child("course").childrenCount
                        for (i in 0 until courseCount) {
                            course.add(
                                datePlanSnapshot.child("course").child(i.toString()).value.toString()
                            )
                        }
                        course.add("")
                        datePlanList.add(
                            DatePlanInfo(
                                title,
                                startDate,
                                endDate,
                                course
                            )
                        )
                    }
                        datePlanList.sortByDescending {
                                it.dateStartDate
                        }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    //실패
                }
            }
            database!!.addValueEventListener(postListener)
        }


    Column(modifier = Modifier) {


        LazyColumnExampleTheme() {
            Surface(
                modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
                color = MaterialTheme.colors.onBackground // app.build.gradle에서 색 지정 가능
            ) {
                Scaffold() { padding ->
                    DatePlans(requestManager, Modifier.padding(padding), datePlanList,navController,listState)
                }
            }
        }

    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DatePlans(requestManager: RequestManager,
              modifier: Modifier = Modifier,
              datePlanList: SnapshotStateList<DatePlanInfo>,
              navController: NavController,
              listState: LazyListState = LazyListState()
)
{
    LazyColumn(modifier = modifier, state = listState) {
        items(datePlanList.size){
            DatePlan(
                datePlanList[it],
                requestManager,
                it,
                navController,
                leaderUID,
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun DatePlan(datePlanInfo: DatePlanInfo,requestManager: RequestManager,num:Int,navController: NavController,leaderUID:String) {
    val swipeSize = 86.dp
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { swipeSize.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(20)
            )
            .aspectRatio(16f / 4f)
            .clickable {
                val datePlanName = datePlanInfo.dateTitle
                navController.navigate("${PrimoScreen.Map.name}/$datePlanName/$leaderUID")
                //getUserOrientation(navController,datePlanName,leaderUID)
            }
            .swipeable(
                swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.8f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Row(modifier = Modifier
            .background(color = Color.Red),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.padding(10.dp,0.dp))
        }
        Row(
            modifier = Modifier
                .offset { if(swipeableState.offset.value.roundToInt() < 0) {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                    else{
                    IntOffset(0, 0)
                }
                }
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(6.dp))
            var url =""
            if(datePlanInfo.course.isNotEmpty()) {
                url = placeListHashMap[datePlanInfo.course[0]]?.imageResource ?:"https://firebasestorage.googleapis.com/v0/b/primo-92b68.appspot.com/o/places%2F%ED%95%98%EB%8A%98.jpg?alt=media&token=dce6f873-3c4c-46e5-bf27-72bcc2a7ddcc"
            }
            GlideImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            {
                it
                    .thumbnail(
                        requestManager
                            .asDrawable()
                            .load(url)
                            .override(64)
                    )
                // .signature(signature)
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Column (
                modifier = Modifier,
            ) {
                Text(
                    text = datePlanInfo.dateTitle,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = datePlanInfo.dateStartDate,
                    color = Color.DarkGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
            }
        }
    }
}
