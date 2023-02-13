package com.example.primo2.screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.primo2.*
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBar(navController: NavController,name: String?,TopBarState:Boolean,topBarText:String,homeListState: LazyListState, datePlanListState: LazyListState) {
    var shadow = 0.dp

    if((name == "Home" && homeListState.canScrollBackward)
        || (name == "DatePlans" && datePlanListState.canScrollBackward ))
    {
        shadow = 10.dp
    }
    if(TopBarState) {
        if (name == "DatePlans") {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = shadow,
            ) {
                Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Start)
                {
                    Text(
                        text = topBarText,
                        modifier = Modifier
                            .padding(12.dp),
                        color = Color.Black,
                        style = Typography.h5.copy(fontSize = 20.sp),
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
        } else {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = shadow
            )
            {
                Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Start)
                {
                    Text(
                        text = topBarText,
                        modifier = Modifier
                            .padding(12.dp),
                        color = Color.Black,
                        style = Typography.h5.copy(fontSize = 20.sp),
                    )
                }
            }
        }
    }
}