package com.droidknights.app2023.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.droidknights.app2023.feature.contributor.navigation.ContributorRoute
import com.droidknights.app2023.feature.contributor.navigation.contributorNavGraph
import com.droidknights.app2023.feature.contributor.navigation.navigateContributor
import com.droidknights.app2023.feature.home.navigation.homeNavGraph
import com.droidknights.app2023.feature.setting.navigation.settingNavGraph

@Composable
internal fun MainScreen(navigator: MainNavigator = rememberMainNavigator()) {
    val navBackStackEntry by navigator.navController.currentBackStackEntryAsState()
    val showBottomBar = when (navBackStackEntry?.destination?.route) {
        ContributorRoute.route -> false
        else -> true
    }
    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
            ) {
                NavHost(
                    navController = navigator.navController,
                    startDestination = navigator.startDestination,
                ) {
                    settingNavGraph()
                    homeNavGraph(
                        padding = padding,
                        onContributorClick = { navigator.navController.navigateContributor() },
                    )
                    // TODO: 각 모듈로 이동
                    val content: @Composable (String) -> Unit = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            content = { Text(text = it) }
                        )
                    }
                    contributorNavGraph(
                        onBackClick = { navigator.navController.popBackStack() }
                    )
                    composable("temp") { content("temp") }
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                MainBottomBar(
                    tabs = MainTab.values().toList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = { navigator.navigate(it) }
                )
            }
        },
    )
}

@Composable
private fun MainBottomBar(
    tabs: List<MainTab>,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
) {
    Row(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(start = 8.dp, end = 8.dp, bottom = 28.dp)
            .fillMaxWidth()
            .height(56.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFDCDCDC), // lightgray
                shape = RoundedCornerShape(size = 28.dp)
            )
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(28.dp))
            .padding(horizontal = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tabs.forEach { tab ->
            MainBottomBarItem(
                tab = tab,
                selected = tab == currentTab,
                onClick = { onTabSelected(tab) },
            )
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(tab.iconResId),
            contentDescription = tab.contentDescription,
            tint = if (selected) Color(0xFF49F300) else Color(0xFFDCDCDC),
            modifier = Modifier.size(34.dp),
        )
    }
}
