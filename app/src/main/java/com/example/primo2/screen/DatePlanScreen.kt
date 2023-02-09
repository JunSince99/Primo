package com.example.primo2.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.DatePlanInfo
import com.example.primo2.getUserOrientation
import com.example.primo2.leaderUID
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography
import com.example.primo2.userOrientation
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePlanScreen(
    navController: NavController,
    requestManager:RequestManager,
    modifier: Modifier = Modifier
) {
    val datePlanList = remember { mutableStateListOf<DatePlanInfo>() }
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
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    //실패
                }
            }
            database!!.addValueEventListener(postListener)
        }


    Column(modifier = Modifier) {

        TopAppBar(backgroundColor = Color.White) {
            Row(modifier = Modifier)
            {
                Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Start)
                {
                    Text(
                        text = "PRIMO",
                        style = Typography.h5.copy(fontSize = 24.sp),
                        modifier = Modifier
                            .padding(12.dp),
                        color = Color.Black
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Column(
                        modifier = Modifier.clickable { navController.navigate(PrimoScreen.SelectDateDate.name) },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.Black
                        )
                        Text(
                            text = "데이트생성",
                            color = Color.Black,
                            style = Typography.h5.copy(fontSize = 10.sp)
                        )
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
                    DatePlans(requestManager, Modifier.padding(padding), datePlanList,navController)
                }
            }
        }

    }
}

@Composable
fun DatePlans(requestManager: RequestManager,
          modifier: Modifier = Modifier,
              datePlanList: SnapshotStateList<DatePlanInfo>,
              navController: NavController
)
{
    LazyColumn(modifier = modifier) {
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


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun DatePlan(datePlanInfo: DatePlanInfo,requestManager: RequestManager,num:Int,navController: NavController,leaderUID:String) {
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

        Box(modifier = Modifier.fillMaxWidth()){
            GlideImage(
                model = "",
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
                            .load("")
                            // .signature(signature)
                            .override(128)
                    )
                // .signature(signature)
            }
        }
        Column(
            modifier = Modifier
                .background(brush = SolidColor(Color.Black), alpha = 0.3f)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = datePlanInfo.dateTitle,
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1.5f))
            ClickableText(
                text = AnnotatedString("데이트 코스 관리"),
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    val datePlanName = datePlanInfo.dateTitle
                    navController.navigate("${PrimoScreen.Map.name}/$datePlanName/$leaderUID")
                }
            )
            Spacer(modifier = Modifier.weight(1.5f))
            Text(
                text = "데이트 코스 삭제",
                color = Color.White,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1.5f))
            Text(
                text = "데이트 코스 확인",
                color = Color.White,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
