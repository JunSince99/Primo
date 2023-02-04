package com.example.primo2.screen

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.example.primo2.R
import com.example.primo2.adapter.placeAdapter
import com.example.primo2.placeList
import com.example.primo2.userOrientation
import com.google.accompanist.permissions.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.*
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.math.roundToInt

@Composable
fun informationPlace(modifier: Modifier = Modifier)
{
    Text(text = "즐겨찾기 페이지", style = MaterialTheme.typography.h4)
}
@OptIn(ExperimentalNaverMapApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun MapScreen(
    naviController: NavController,
    requestManager:RequestManager,
    datePlanName:String?,
    modifier: Modifier = Modifier
){
    Log.e("뭘까", ""+datePlanName)
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

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var bottomNaviSize by remember { mutableStateOf(65.dp) }
    var bottomNaviTitle by remember { mutableStateOf("") }
    var bottomNaviInfo by remember { mutableStateOf("") }
    var bottomNaviPaint by remember { mutableStateOf("") }

    var showMapInfo by remember { mutableStateOf(false) }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent(bottomNaviTitle,bottomNaviPaint,bottomNaviInfo,requestManager,showMapInfo)
        },
        sheetPeekHeight = bottomNaviSize,
    )
    {
        modifier.padding(it)

        Box(
            Modifier
                .fillMaxSize()) {
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
                    showMapInfo = false
                    bottomNaviSize = 100.dp
                    Log.e("이 곳의 경도 위도는?", "" + coord.latitude + "," + coord.longitude)
                }
            )
            {

                for (i in 0 until placeList.size) {
                    var fitness:Double = fitnessCalc(userOrientation,i)
                    Marker(
                        icon = OverlayImage.fromResource(R.drawable.ic_baseline_place_24),
                        width = 40.dp,
                        height = 40.dp,
                        state = MarkerState(
                            position = LatLng(
                                placeList[i].latitude,
                                placeList[i].longitude
                            )
                        ),
                        captionText = placeList[i].placeName+"\n"+"적합도 : " + fitness.roundToInt() + "%",
                        captionMinZoom = 12.2,
                        minZoom = 12.2,
                        onClick = { overlay ->
                            bottomNaviSize = 65.dp
                            bottomNaviInfo = placeList[i].information
                            bottomNaviTitle = placeList[i].placeName
                            bottomNaviPaint = placeList[i].imageResource
                            showMapInfo = true
                            true
                        },
                        tag = i,
                        zIndex = fitness.roundToInt() // 겹칠때 적합도 높은게 위로 가게
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
@Composable
fun BottomSheetBeforeSlide(title: String) { // 위로 스와이프 하기전에 보이는거
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .height(65.dp)
            .background(color = Color.White)
            .padding(start = 0.dp, top = 5.dp), verticalAlignment = Alignment.Top
    ) {
        Spacer(modifier = Modifier.width(0.dp))
            Column(modifier = Modifier, verticalArrangement = Arrangement.Top) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Cursive
                )
                Text(
                    text = "인천 연수구 송도동 24-5",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Cursive
                )
            }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .height(65.dp)
                .background(color = Color.White)
                .padding(start = 0.dp, top = 5.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End

        ) {

            Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BottomSheetContent(title: String, paint:String, info:String,requestManager: RequestManager,showMapInfo:Boolean) { // 스와이프 한후에 보이는 전체
    val context = LocalContext.current
    Column {
        if(showMapInfo) {
            BottomSheetBeforeSlide(title)
            GlideImage(
                model = paint, contentDescription = "", modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(), contentScale = ContentScale.Crop
            )
            {
                it
                    .thumbnail(
                        requestManager
                            .asDrawable()
                            .load(paint)
                            // .signature(signature)
                            .override(64)
                    )
                // .signature(signature)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = info, fontFamily = FontFamily.Cursive)
        }
        else{
            Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ) {

                Card(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(shape = RoundedCornerShape(20),
                            elevation = 5.dp)
                ) {
                    Text(modifier = Modifier.fillMaxWidth().fillMaxHeight(), textAlign = TextAlign.Center, text = "센트럴 파크")
                }
                Card(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(shape = RoundedCornerShape(20),
                            elevation = 5.dp)
                ) {
                    Text(modifier = Modifier.fillMaxWidth().fillMaxHeight(), textAlign = TextAlign.Center, text = "송도 셜록홈즈 방탈출")
                }
                Card(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(shape = RoundedCornerShape(20),
                            elevation = 5.dp)
                ) {
                    Text(modifier = Modifier.fillMaxWidth().fillMaxHeight(), textAlign = TextAlign.Center, text = "솔찬 공원")
                }
            }
        }
    }
}


fun fitnessCalc(userOrientation: HashMap<String, Double>,num :Int) : Double{
    var fitness = 0.0
    //fitness += 20 + ((userOrientation["IE"]!! * placeList[num].static) - (userOrientation["IE"]!! * placeList[num].active))*10
    //fitness += 20 +((userOrientation["NS"]!! * placeList[num].nature) - (userOrientation["NS"]!! * placeList[num].city))*10
    //fitness += 20 +((userOrientation["FT"]!! * placeList[num].focusFood) - (userOrientation["FT"]!! * placeList[num].focusTour))*10
   //fitness += 20 +((userOrientation["PJ"]!! * placeList[num].lazy) - (userOrientation["PJ"]!! * placeList[num].faithful))*10

    if(fitness > 100)
    {
        fitness = 100.0
    }
    return fitness
}
@Preview(showBackground = true)
@Composable
fun BottomSheetListItemPreview() {
    BottomSheetBeforeSlide(title = "센트럴 파크")
}

/*
@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview(requestManager:RequestManager) {
    BottomSheetContent("센트럴파크","https://firebasestorage.googleapis.com/v0/b/primo-92b68.appspot.com/o/places%2F%EC%86%94%EC%B0%AC%EA%B3%B5%EC%9B%90.jpg?alt=media&token=cb9ace94-0d86-4cf1-8065-b6781b8ea30d","센트럴 파크에서 재밌게 노는법!",requestManager)
}

 */



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




