package com.example.primo2.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun NavigationBar(navController: NavController){
    BottomNavigation(
        modifier = Modifier,
        backgroundColor = Color.White,
        contentColor = Color.Black,
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
            selectedContentColor = MaterialTheme.colors.onPrimary,
            unselectedContentColor = Color.Gray,
            selected = currentDestination?.hierarchy?.any{it.route == "Home"} == true,
            onClick = { navController.navigate("Home") }
        ) // 홈화면
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector =  Icons.Default.Search,
                    contentDescription = null
                )
            },
            selectedContentColor = Color.Black, // 계속 하얀색되서 잠깐 바꿔놈 고쳐줘
            unselectedContentColor = Color.Gray,
            selected = currentDestination?.hierarchy?.any{it.route == "Map"} == true,
            onClick = { navController.navigate("Map") }
        ) // 서치 화면
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector =  Icons.Default.Star,
                    contentDescription = null
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray,
            selected = currentDestination?.hierarchy?.any{it.route == "Favorites"} == true,
            onClick = { navController.navigate("Favorites") }
        ) // 즐겨찾기?
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector =  Icons.Default.Person,
                    contentDescription = null
                )
            },
            selectedContentColor = Color.Black,
            unselectedContentColor = Color.Gray,
            selected = currentDestination?.hierarchy?.any{it.route == "ManageAccount"} == true,
            onClick = { navController.navigate("ManageAccount") }
        ) // 계정 관리
    }
}