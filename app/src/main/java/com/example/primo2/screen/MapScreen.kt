package com.example.primo2.screen

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.primo2.R
import com.example.primo2.adapter.placeAdapter
import com.example.primo2.placeList
import com.google.accompanist.permissions.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.OverlayImage
@Composable
fun informationPlace(modifier: Modifier = Modifier)
{
    Text(text = "즐겨찾기 페이지", style = MaterialTheme.typography.h4)
}
@OptIn(ExperimentalNaverMapApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    naviController: NavController,
    modifier: Modifier = Modifier
){

    val infoWindow = InfoWindow()
    val context = LocalContext.current
    val adapter = placeAdapter(context)
    infoWindow.adapter = adapter
    /*
    infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(context) {
        override fun getText(infoWindow: InfoWindow): CharSequence {
            return infoWindow.marker?.tag as CharSequence? ?: ""
        }
    }*/

    var mapProperties by remember {
        mutableStateOf(
            MapProperties(locationTrackingMode = LocationTrackingMode.Follow
                    ,maxZoom = 20.0, minZoom = 5.0),
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true, isIndoorLevelPickerEnabled = true)
        )
    }
    Scaffold(
        bottomBar = {
            NavigationBar(naviController)
        },
        backgroundColor = Color.White
    )
    {
        modifier.padding(it)
        Box(Modifier.fillMaxSize()) {
            val cameraPositionState = rememberCameraPositionState()
            val position by remember {
                derivedStateOf {
                    cameraPositionState.position
                }
            }
            /*
        LaunchedEffect(cameraPositionState.isMoving){
            if(cameraPositionState.isMoving)
            {
                Log.e("카메라 움직임", "줌레벨 :")
                Log.e("카메라 움직임", "줌레벨 : "+cameraPositionState.position.zoom)
            }
            else{
                Log.e("카메라 멈춤", "줌레벨 :")
                Log.e("카메라 멈춤", "줌레벨 : "+cameraPositionState.position.zoom)
            }
        }*/
            NaverMap(cameraPositionState = cameraPositionState,
                locationSource = rememberFusedLocationSource(),
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMapClick = { _, coord ->
                    Log.e("이 곳의 경도 위도는?", "" + coord.latitude + "," + coord.longitude)
                }
            )
            {

                for (i in 0 until placeList.size) {
                    Marker(
                        icon = OverlayImage.fromResource(R.drawable.map_markerx),// 맵 마커 이미지인데 일단 임시로 암거나 넣어놈
                        state = MarkerState(
                            position = LatLng(
                                placeList[i].latitude,
                                placeList[i].longitude
                            )
                        ),
                        captionText = placeList[i].name,
                        captionMinZoom = 13.0,
                        minZoom = 12.0,
                        onClick = { overlay ->
                            infoWindow.open(overlay)
                            true
                        },
                        tag = placeList[i].information
                    )
                }
                // Marker(state = rememberMarkerState(position = BOUNDS_1.northEast))
            }
            Column {
                //ShowLocationPermission()
            }
        }
    }

}



/*
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowLocationPermission(){

    val cameraPermissionState = rememberMultiplePermissionsState(
        listOf( android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
    )

    if (cameraPermissionState.allPermissionsGranted) {
        Text("위치정보 권한이 부여됨")
    } else {
        val textToShow = if (cameraPermissionState.shouldShowRationale) {
            // 사용자가 권한 요청을 거부했지만 근거를 제시할 수 있는 경우, 앱에 이 권한이 필요한 이유를 친절하게 설명합니다.
            "현재 위치를 사용하기 위해선 위치정보를 허용해주세요."
        } else {
            // 사용자가 이 기능을 처음 사용하거나, 사용자에게 이 권한을 다시 묻고 싶지 않은 경우 권한이 필요하다고 설명합니다.
            "현재 위치를 불러오려면 위치정보가 필요합니다." +
                    "권한을 부여해주세요."
        }
        Text(textToShow)
        Button(onClick = { cameraPermissionState.launchMultiplePermissionRequest() }) {
            Text("권한 요청")
        }
    }
}

*/




