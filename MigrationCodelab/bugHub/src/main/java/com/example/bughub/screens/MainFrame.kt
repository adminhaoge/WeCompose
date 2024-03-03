package com.example.bughub.screens

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.bughub.data.bean.NavigationItem

@Composable
fun MainFrame() {
    val navigationItems = remember {
        mutableStateListOf(
            NavigationItem(title = "学习", icon = Icons.Filled.Home),
            NavigationItem(title = "任务", icon = Icons.Filled.DateRange),
            NavigationItem(title = "我的", icon = Icons.Filled.Person)
        )
    }

    var currentNavigationIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(bottomBar = {
        BottomNavigation(backgroundColor = MaterialTheme.colors.surface) {
            navigationItems.forEachIndexed { index, navigationItem ->
                BottomNavigationItem(
                    selected = currentNavigationIndex == index,
                    onClick = {
                        currentNavigationIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = navigationItem.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = navigationItem.title)
                    },
                    selectedContentColor = Color(0xff149ee7),
                    unselectedContentColor = Color(0xFF999999)
                )
            }
        }
    }) { _ ->
        Text(text = "current navigation item: $currentNavigationIndex")
    }
}

@Preview(showBackground = true)
@Composable
fun MainFramePreview() {
    MainFrame()
}