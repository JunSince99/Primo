package com.example.primo2.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.*
import com.google.type.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.compose.*
import com.naver.maps.map.compose.LocationTrackingMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalNaverMapApi::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier
){


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

    Box(Modifier.fillMaxSize()) {
        NaverMap(locationSource = rememberFusedLocationSource(),properties = mapProperties, uiSettings = mapUiSettings)
        Column {
            //ShowLocationPermission()
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
@OptIn(ExperimentalNaverMapApi::class)
@Preview
@Composable
fun MapScreenPreview(
    modifier: Modifier = Modifier
){
    NaverMap(
        modifier = Modifier.fillMaxSize()
    )
    {
        val context = LocalContext.current
        var naverMap:NaverMap? = null

        DisposableMapEffect(LocalLifecycleOwner){ map->
            naverMap = map
            onDispose {
            }

        }


        var mapProperties by remember {
            mutableStateOf(
                MapProperties(maxZoom = 10.0, minZoom = 5.0)
            )
        }
        var mapUiSettings by remember {
            mutableStateOf(
                MapUiSettings(isLocationButtonEnabled = false)
            )
        }

        Box(Modifier.fillMaxSize()) {
            NaverMap(properties = mapProperties, uiSettings = mapUiSettings)
            Column {
                Button(onClick = {
                    mapProperties = mapProperties.copy(
                        isBuildingLayerGroupEnabled = !mapProperties.isBuildingLayerGroupEnabled
                    )
                }) {
                    Text(text = "Toggle isBuildingLayerGroupEnabled")
                }
                Button(onClick = {
                    mapUiSettings = mapUiSettings.copy(
                        isLocationButtonEnabled = !mapUiSettings.isLocationButtonEnabled
                    )
                }) {
                    Text(text = "Toggle isLocationButtonEnabled")
                }
            }
        }

        val cameraUpdate = CameraUpdate.scrollTo(
            com.naver.maps.geometry.LatLng(
                17.5666102,
                126.9783881
            )
        )
            .reason(3)
            .animate(CameraAnimation.Easing, 2000)
            .finishCallback {
                Toast.makeText(context, "완료", Toast.LENGTH_SHORT).show()
            }
            .cancelCallback {
                Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show()
            }

        naverMap!!.moveCamera(cameraUpdate)

        Log.e("","xx")
    }
}



