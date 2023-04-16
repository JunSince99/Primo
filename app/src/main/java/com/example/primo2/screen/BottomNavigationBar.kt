package com.example.primo2.screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.primo2.*
import com.example.primo2.R
import com.example.primo2.ui.theme.LazyColumnExampleTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationBar(navController: NavController,bottomBarState:Boolean) {
    if (bottomBarState) {
        LazyColumnExampleTheme() {
            BottomNavigation(
                modifier = Modifier.height(50.dp),
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.primary,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Color.Gray,
                    selected = currentDestination?.hierarchy?.any { it.route == "Home" } == true,
                    onClick = { navController.navigate("Home") }
                ) // 홈화면
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_calendar_month_24),
                            contentDescription = null
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Color.Gray,
                    selected = currentDestination?.hierarchy?.any { it.route == "DatePlans" } == true,
                    onClick = { navController.navigate("DatePlans") { popUpTo("Home") } }
                ) // 달력 화면
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Color.Gray,
                    selected = currentDestination?.hierarchy?.any { it.route == "Favorites" } == true,
                    onClick = { if(partnerName != null && userOrientation.isNotEmpty()) {
                        navController.navigate(PrimoScreen.PlaceBattle.name) { popUpTo("Home") } } }
                ) // 장소 월드컵
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Color.Gray,
                    selected = currentDestination?.hierarchy?.any { it.route == "ManageAccount" } == true,
                    onClick = {
                            if(partnerName!= null){navController.navigate("ManageAccount") { popUpTo("Home") }}
                    }
                ) // 계정 관리
            }
        }
    }
}