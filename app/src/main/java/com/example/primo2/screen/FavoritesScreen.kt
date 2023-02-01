package com.example.primo2.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.ui.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var bottomNaviSize by remember { mutableStateOf(0.dp) }
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
}
