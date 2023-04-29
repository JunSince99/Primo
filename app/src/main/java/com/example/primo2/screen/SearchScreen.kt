package com.example.primo2.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.*
import com.example.primo2.ui.theme.moreLightGray
import com.example.primo2.ui.theme.spoqasans
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.absoluteValue
import kotlin.math.min

@Composable
fun Search(requestManager: RequestManager,navController: NavController) {
    var searchKeyword by remember { mutableStateOf("") }
    val searchPlaceList = remember { mutableStateListOf<Int>() }
    LaunchedEffect(searchKeyword)
    {
        if(searchKeyword.isBlank())
        {
            searchPlaceList.clear()
        }
        if (searchKeyword.isNotBlank()) {
            searchPlaceList.clear()
            for (i in 0 until placeList.size) {
                if (placeList[i].placeName.contains(searchKeyword)) {
                    searchPlaceList.add(i)
                }
            }
        }
    }

    val database = Firebase.database.reference
        .child("DatePlan")
        .child(leaderUID)
    val courseList = remember { mutableStateListOf<String>() }
    LaunchedEffect(true) {
        if (courseList.isEmpty()) {
            database.child(entireDatePlanName!!).child("course").get().addOnSuccessListener {
                courseList.clear()
                for (i in 0 until it.childrenCount) {
                    courseList.add(it.child(i.toString()).value.toString())
                }
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {
        Column {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = searchKeyword,
                onValueChange = { text ->
                    searchKeyword = text
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "검색",
                        color = Color.Gray
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = Color.Black
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = {
                            if (searchKeyword.isNotEmpty()) {
                                searchKeyword = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = Color.Black
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = moreLightGray,
                    unfocusedIndicatorColor = moreLightGray
                )
            )
            if(searchKeyword.isEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                {
                    items(4){ mode ->
                        if(mode == 0) {
                            Places(requestManager, navController, mode,courseList)
                        }
                        else {
                            if (partnerName != "") {
                                Places(requestManager, navController, mode,courseList)
                            }
                        }
                    }
                }
            }
            else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    items(searchPlaceList) { item ->
                        Place(item, requestManager, navController,courseList)
                    }
                }
            }
        }
    }
}

@Composable
fun Places(requestManager: RequestManager,navController: NavController, mode:Int,courseList: SnapshotStateList<String>){
    var comment = ""
    var howIndex by remember { mutableStateOf(3) }
    val sortPlaceList:ArrayList<PlaceInfo> = arrayListOf()

    sortPlaceList.addAll(placeList)
    when (mode) {
        0 -> {
            comment = myName + "님 추천 장소"
        }
        1 -> {
            comment = partnerName + "님 추천 장소"
        }
        2 -> {
            comment = "커플 추천 장소"
        }
        3 -> {
            comment = "색다른 데이트 장소"
        }
    }

    Spacer(modifier = Modifier.padding(8.dp))
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = comment,
            fontSize = 20.sp,
            fontFamily = spoqasans,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(4.dp))
        if(mode == 0) {
            //본인 선호도순 정렬
            sortPlaceList.sortBy {
                var total = 0
                for ((key, value) in userOrientation) {
                    if (key != "편의시설" && key != "대중교통") {
                        if (it.placeHashMap!!.containsKey(key)) {
                            total += (value - it.placeHashMap[key].toString().toInt()).absoluteValue
                        }
                    }
                }
                if(courseList.isNotEmpty()){
                    val course = placeListHashMap[courseList[courseList.lastIndex]]
                    val distanceScore = getDistance(course!!.latitude,course.longitude,it.latitude,it.longitude)
                    if(distanceScore > 1000) {
                        total += distanceScore / 5
                    }
                }
                total
            }
        }
        else if(mode == 1){
            sortPlaceList.sortBy {
                var total = 0
                for ((key, value) in partnerOrientation) {
                    if (key != "편의시설" && key != "대중교통") {
                        if (it.placeHashMap!!.containsKey(key)) {
                            total += (value - it.placeHashMap[key].toString().toInt()).absoluteValue
                        }
                    }
                }
                if(courseList.isNotEmpty()){
                    val course = placeListHashMap[courseList[courseList.lastIndex]]
                    val distanceScore = getDistance(course!!.latitude,course.longitude,it.latitude,it.longitude)
                    if(distanceScore > 1000) {
                        total += distanceScore / 5
                    }
                }
                total
            }
        }
        else if(mode == 2){
            sortPlaceList.sortBy {
                var total = 0
                for ((key, value) in partnerOrientation) {
                    if (key != "편의시설" && key != "대중교통") {
                        if (it.placeHashMap!!.containsKey(key)) {
                            total += (value - it.placeHashMap[key].toString().toInt()).absoluteValue
                        }
                    }
                }
                for ((key, value) in userOrientation) {
                    if (key != "편의시설" && key != "대중교통") {
                        if (it.placeHashMap!!.containsKey(key)) {
                            total += (value - it.placeHashMap[key].toString().toInt()).absoluteValue
                        }
                    }
                }
                if(courseList.isNotEmpty()){
                    val course = placeListHashMap[courseList[courseList.lastIndex]]
                    val distanceScore = getDistance(course!!.latitude,course.longitude,it.latitude,it.longitude)
                    if(distanceScore > 1000){
                        total += distanceScore/5
                    }
                }
                total
            }
        }
        else if(mode == 3){
            sortPlaceList.sortByDescending {
                var total = 0
                for ((key, value) in partnerOrientation) {
                    if (key != "편의시설" && key != "대중교통") {
                        if (it.placeHashMap!!.containsKey(key)) {
                            total += (value - it.placeHashMap[key].toString().toInt()).absoluteValue
                        }
                    }
                }
                for ((key, value) in userOrientation) {
                    if (key != "편의시설" && key != "대중교통") {
                        if (it.placeHashMap!!.containsKey(key)) {
                            total += (value - it.placeHashMap[key].toString().toInt()).absoluteValue
                        }
                    }
                }
                if(courseList.isNotEmpty()){
                    val course = placeListHashMap[courseList[courseList.lastIndex]]
                    val distanceScore = getDistance(course!!.latitude,course.longitude,it.latitude,it.longitude)
                    if(distanceScore > 1000){
                        total += distanceScore/5
                    }
                }
                total
            }
        }
        Column(modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )) {
            val endIndex = min(howIndex,sortPlaceList.size)
            for (i in 0 until endIndex) {
                val index = placeList.indexOf(sortPlaceList[i])

                Place(index, requestManager, navController,courseList)
            }
        }
        Button(
            onClick = { howIndex += 5 },
            elevation = ButtonDefaults.elevation(
                defaultElevation = 1.dp,
                pressedElevation = 0.dp
            ),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = moreLightGray,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 355.dp, 50.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "더보기",
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Place(item:Int,requestManager: RequestManager,navController: NavController,courseList:SnapshotStateList<String>){
    val context = LocalContext.current
    Row {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                .clickable {
                    navController.navigate("${PrimoScreen.PlaceDetailScreen.name}/$item")
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                val url = placeList[item].imageResource
                Row {
                    GlideImage(
                        model = url,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(50.dp),
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
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                    ) {
                        Text(
                            text = placeList[item].placeName,
                            color = Color.Black,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row {
                            for (i in 0 until 4) {
                                placetag(placeList[item].toptag[i], 10.sp)
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        val database = Firebase.database.reference
                        .child("DatePlan")
                        .child(leaderUID)
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
                              },
                    modifier = Modifier
                        .size(width = 50.dp,height = 30.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = moreLightGray,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(1.dp)
                ) {
                    Text(
                        text = "추가",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                    )
                }
            }
        }
//        Button(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .weight(0.7f)
//                .aspectRatio(1f / 1f),
//            elevation = ButtonDefaults.elevation(
//                defaultElevation = 1.dp,
//                pressedElevation = 0.dp
//            ),
//            shape = RoundedCornerShape(20.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color.White,
//                contentColor = Color.Black
//            )
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = "장소 추가",
//                modifier = Modifier.size(20.dp)
//            )
//        }
    }
}

@Composable
fun placetag(tagname : String, scale : TextUnit){
    Box(
        modifier = Modifier
            .border(
                1.dp,
                Color.LightGray,
                RoundedCornerShape(60)
            )
    ) {
        Text(
            text = tagname,
            color = Color.Black,
            fontSize = scale,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(6.dp)
        )
    }
    Spacer(modifier = Modifier.padding(4.dp))
}