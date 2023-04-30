package com.example.primo2.screen

import android.graphics.Bitmap
import android.graphics.Color
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.primo2.R
import com.example.primo2.placeList
import com.example.primo2.placeListHashMap
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder


@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapImsi(
    modifier: Modifier = Modifier
){
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(locationTrackingMode = LocationTrackingMode.Follow
                ,maxZoom = 20.0, minZoom = 5.0),
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(isLocationButtonEnabled = true, isIndoorLevelPickerEnabled = true)
        )
    }
    val cameraPositionState = rememberCameraPositionState()
    val position by remember {
        derivedStateOf {
            cameraPositionState.position
        }
    }


    Box(
        Modifier
            .fillMaxSize()) {
                NaverMap(cameraPositionState = cameraPositionState,
                    locationSource = rememberFusedLocationSource(),
                    properties = mapProperties,
                    uiSettings = mapUiSettings,
                    onMapClick = { _, coord ->
                        Log.e("이 곳의 경도 위도는?", "" + coord.latitude + "," + coord.longitude)
                    }


                )
                {

                    val latlist: ArrayList<LatLng> = arrayListOf()
                    for (i in 0 until placeList.size) {

                        Marker(
                            icon = OverlayImage.fromResource(R.drawable.ic_baseline_circle_24),
                            width = 10.dp,
                            height = 10.dp,
                            anchor = Offset(0.45f, 0.45f),
                            state = MarkerState(
                                position = LatLng(
                                    placeList[i].latitude,
                                    placeList[i].longitude
                                )
                            ),
                            captionText = placeList[i].placeName,
                            //captionColor = Color.Green,
                            tag = i,
                        )
                    }
                    // Marker(state = rememberMarkerState(position = BOUNDS_1.northEast))

                }
            }

}
