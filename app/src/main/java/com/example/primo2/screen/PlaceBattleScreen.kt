package com.example.primo2.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.placeList
import com.example.primo2.placeListHashMap
import com.example.primo2.rankTaste
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaceBattle(requestManager:RequestManager) {
    var firstPlace by remember { mutableStateOf(0) }
    var secondPlace by remember { mutableStateOf(0) }
    var type by remember { mutableStateOf(0) }
    LaunchedEffect(true) {
        val (first,second, typecheck) = settingPlaces()
        firstPlace = first
        secondPlace = second
        type = typecheck
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var showFirstPlaceList = placeList[firstPlace]
        var showSecondPlaceList = placeList[secondPlace]

        if(type == 1) {
            showFirstPlaceList = placeListHashMap[rankTaste[firstPlace]]!!
            showSecondPlaceList = placeListHashMap[rankTaste[secondPlace]]!!
        }
        Text(showFirstPlaceList.placeName, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 20.sp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .clickable {
                    val firstPlaceIndex = rankTaste.indexOf(showFirstPlaceList.placeID)
                    val secondPlaceIndex = rankTaste.indexOf(showSecondPlaceList.placeID)
                    Log.e("퍼스트","" + firstPlaceIndex)
                    Log.e("세컨드","" + secondPlaceIndex)
                    if(firstPlaceIndex != -1 && secondPlaceIndex != -1)
                    {
                        if(secondPlaceIndex < firstPlaceIndex)
                        {
                            val tmp = rankTaste[secondPlaceIndex]
                            rankTaste[secondPlaceIndex] = rankTaste[firstPlaceIndex]
                            rankTaste[firstPlaceIndex] = tmp
                        }
                    }
                    else if(secondPlaceIndex != -1)
                    {
                        rankTaste.add(secondPlaceIndex, showFirstPlaceList.placeID)
                    }
                    else if(firstPlaceIndex == -1){
                        rankTaste.add(0, showFirstPlaceList.placeID)
                        rankTaste.add(rankTaste.size, showSecondPlaceList.placeID)
                    }
                    saveTasteRank()
                    val (first,second,checktype) = settingPlaces()
                    firstPlace = first
                    secondPlace = second
                    type = checktype
                }
        ) {
            val uri = showFirstPlaceList.imageResource
            GlideImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
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
        Text(text = showSecondPlaceList.placeName, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 20.sp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable {
                    val firstPlaceIndex = rankTaste.indexOf(showFirstPlaceList.placeID)
                    val secondPlaceIndex = rankTaste.indexOf(showSecondPlaceList.placeID)
                    if(firstPlaceIndex != -1 && secondPlaceIndex != -1)
                    {
                        if(secondPlaceIndex > firstPlaceIndex)
                        {
                            val tmp = rankTaste[secondPlaceIndex]
                            rankTaste[secondPlaceIndex] = rankTaste[firstPlaceIndex]
                            rankTaste[firstPlaceIndex] = tmp
                        }
                    }
                    else if(firstPlaceIndex != -1)
                    {
                        rankTaste.add(firstPlaceIndex, showSecondPlaceList.placeID)
                    }
                    else if(secondPlaceIndex == -1){
                        rankTaste.add(0, showSecondPlaceList.placeID)
                        rankTaste.add(rankTaste.size, showFirstPlaceList.placeID)
                    }
                    saveTasteRank()
                    val (first,second,checktype) = settingPlaces()
                    firstPlace = first
                    secondPlace = second
                    type = checktype
                }
        ) {
            val uri = showSecondPlaceList.imageResource
            GlideImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
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

fun settingPlaces():Triple<Int, Int, Int>{
    val random = Random()
    var firstPlace = 0
    var secondPlace = 0
    var type = 0
    if(rankTaste.size >= 4) {
        val selectWhat = random.nextInt(2)
        if(selectWhat == 0) {
            firstPlace = random.nextInt(rankTaste.size)
            secondPlace = random.nextInt(rankTaste.size)
            while (secondPlace == firstPlace) {
                secondPlace = random.nextInt(rankTaste.size)
            }
            type = 1
            Log.e("내부", "내부경쟁" + firstPlace + " "+ secondPlace)
        }
        else{
            firstPlace = random.nextInt(placeList.size)
            secondPlace = random.nextInt(placeList.size)
            while (secondPlace == firstPlace) {
                secondPlace = random.nextInt(placeList.size)
            }
            type = 0
            Log.e("전체","전체경쟁" + firstPlace + " "+ secondPlace )
        }
    }
    else {
        firstPlace = random.nextInt(placeList.size)
        secondPlace = random.nextInt(placeList.size)
        while (secondPlace == firstPlace) {
            secondPlace = random.nextInt(placeList.size)
        }
        type = 0
        Log.e("전체","전체경쟁")
    }

    return Triple(firstPlace, secondPlace, type)
}

fun saveTasteRank()
{
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.uid)

    db.runTransaction { transaction ->
        val snapshot = transaction.get(docRef)
        transaction.update(docRef, "rankTaste", rankTaste)
        rankTaste
    }
}