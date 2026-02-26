package com.manus.lara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manus.lara.ui.LaraViewModel
import com.manus.lara.ui.screens.config.ConfigScreen
import com.manus.lara.ui.screens.dashboard.DashboardScreen
import com.manus.lara.ui.screens.dashboard.TadaScreen
import com.manus.lara.ui.screens.onboarding.OnboardingScreen
import com.manus.lara.ui.theme.LaraTheme
import com.manus.lara.ui.theme.SuccessMint

class MainActivity : ComponentActivity() {
    private val viewModel: LaraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaraTheme {
                val navController = rememberNavController()
                val isLoading by viewModel.isLoading.collectAsState()
                
                val startDestination = remember {
                    when {
                        !viewModel.isConfigured() -> "config"
                        !viewModel.isOnboardingComplete() -> "onboarding"
                        else -> "dashboard"
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("config") {
                            ConfigScreen { apiKey ->
                                viewModel.saveApiKey(apiKey)
                                navController.navigate("onboarding") {
                                    popUpTo("config") { inclusive = true }
                                }
                            }
                        }
                        composable("onboarding") {
                            OnboardingScreen { rooms, hasKids, hasPets, appliances ->
                                viewModel.completeOnboarding(rooms, hasKids, hasPets, appliances)
                                navController.navigate("dashboard") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        }
                        composable("dashboard") {
                            val tasks by viewModel.todayTasks.collectAsState(initial = emptyList())
                            DashboardScreen(
                                tasks = tasks,
                                onToggleTask = { viewModel.toggleTask(it) },
                                onUnforeseen = { viewModel.handleUnforeseen(it) },
                                onNavigateToTada = { navController.navigate("tada") }
                            )
                        }
                        composable("tada") {
                            val completedTasks by viewModel.completedTasks.collectAsState(initial = emptyList())
                            TadaScreen(completedTasks = completedTasks) {
                                navController.popBackStack()
                            }
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = SuccessMint)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.loading_gemini),
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
