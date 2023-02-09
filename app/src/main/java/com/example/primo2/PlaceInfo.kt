package com.example.primo2

import android.graphics.PointF
import android.location.Location
import android.util.Log
import android.widget.ImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class PlaceInfo(
    val placeID: String = "",
    val placeName: String = "",
    val information: String = "",
    val imageResource: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeHashMap: HashMap<String,Double>? = null
)
val placeList: ArrayList<PlaceInfo> = ArrayList()
fun getPlaceInfo(){
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    if(user != null) {
        db.collection("places")
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents)
                {
                    val imageResource = document.data["imageResource"] as String
                    val placeID = document.data["id"] as String
                    val placeName = document.data["placeName"] as String
                    val placeHashMap = document.data["placeHashMap"]as HashMap<String, Double>
                    val latitude = document.data["latitude"] as Double
                    val longitude = document.data["longitude"] as Double
                    val information = document.data["information"] as String
                    placeList.add(PlaceInfo(placeID,placeName,information,imageResource,latitude,longitude,placeHashMap))
                }
            }

            .addOnFailureListener { exception ->
                Log.e("오류","플레이스 오류 발생")
            }
    }
}
/*
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
    val faithful:Double = 0.0,
    val imageResource:Int = 0
)
fun getPlaceInfo()
{
    placeList.add(PlaceInfo("센트럴파크",
        "도심속에서 산책하기 좋은 센트럴 파크!",
        37.39132, 126.64057,
        0.9,0.1,
        0.3,0.7,
        0.1,0.9,
        0.9,0.1,
        R.drawable.place_centralpark)
        )
    placeList.add(PlaceInfo("솔찬 공원",
        "예쁜 바다와 감성 카페가 있는 공원!",
        37.3724716115434,126.62812397607729,
        0.9,0.1,
        0.8,0.2,
        0.7,0.3,
        0.9,0.1,
        R.drawable.place_solchanpark
    ))
    placeList.add(
        PlaceInfo("송도 한옥마을",
    "송도에서 한옥을 즐길 수 있는 곳",
            37.390008721815335,126.64057524403597,
            1.0, 0.0,
            0.7,0.3,
            0.7,0.3,
            0.9,0.1
        )
    )

    placeList.add(
        PlaceInfo("트라이볼",
            "다양한 음악공연과 문화예술 공연을 즐겨보세요",
            37.394168828384664,126.63497342447238,
            0.85, 0.15,
            0.1,0.9,
            0.1,0.9,
            0.8,0.2
        )
    )

    placeList.add(
        PlaceInfo("커넬워크",
            "쇼핑과 먹거리를 즐길 수 있는 거리",
            37.40217199808477,126.64038062556165,
            0.65, 0.35,
            0.05,0.95,
            0.8,0.2,
            0.3,0.7
        )
    )

    placeList.add(
        PlaceInfo("트리플 스트리트",
            "쇼핑과 먹거리, 문화를 즐길 수 있는 거리",
            37.38074958740326,126.6597777247668,
            0.65, 0.35,
            0.05,0.95,
            0.85,0.15,
            0.3,0.7
        )
    )


}
*/