package com.example.primo2

import android.graphics.PointF
import android.location.Location

data class PlaceInfo(
    val name: String? = null,
    val information: String? = null,
    val latitude: Double = 0.0,
    val longitude:Double = 0.0,
    val static:Double = 0.0,
    val active:Double = 0.0,
    val nature:Double = 0.0,
    val city:Double = 0.0,
    val focusFood:Double = 0.0,
    val focusTour:Double = 0.0,
    val lazy:Double = 0.0,
    val faithful:Double = 0.0

)
val placeList: ArrayList<PlaceInfo> = ArrayList()
fun getPlaceInfo()
{
    placeList.add(PlaceInfo("센트럴파크",
        "도심속에서 산책하기 좋은 센트럴 파크!",
        37.39132, 126.64057,
        0.9,0.1,
        0.3,0.7,
        0.1,0.9,
        0.9,0.1))
    placeList.add(PlaceInfo("솔찬 공원",
        "예쁜 바다와 감성 카페가 있는 공원!",
        37.3724716115434,126.62812397607729,
        0.9,0.1,
        0.8,0.2,
        0.7,0.3,
        0.9,0.1
    ))
}
