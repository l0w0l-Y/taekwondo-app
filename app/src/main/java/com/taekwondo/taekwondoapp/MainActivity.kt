package com.taekwondo.taekwondoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.taekwondo.featurefighter.presentation.CreateFighterScreen
import com.taekwondo.featurefighter.presentation.CreateFighterViewModel
import com.taekwondo.featuremain.presentation.MainScreen
import com.taekwondo.corenavigation.AuthDirection
import com.taekwondo.corenavigation.CreateEventDirection
import com.taekwondo.corenavigation.CreateFighterDirection
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.corenavigation.ReadEventDirection
import com.taekwondo.corenavigation.ReadFighterDirection
import com.taekwondo.corenavigation.RegisterDirection
import com.taekwondo.corenavigation.UpdateEventDirection
import com.taekwondo.corenavigation.UpdateEventFighterDirection
import com.taekwondo.corenavigation.UpdateFighterDirection
import com.taekwondo.coretheme.AppTheme
import com.taekwondo.featureauth.presentation.auth.AuthScreen
import com.taekwondo.featureauth.presentation.register.RegisterScreen
import com.taekwondo.featureevent.presentation.event.EventScreen
import com.taekwondo.featureevent.presentation.fighters.UpdateEventFighterScreen
import com.taekwondo.featureevent.presentation.update.CreateEventScreen
import com.taekwondo.featureevent.presentation.update.CreateEventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold {
                    val viewModel: MainViewModel = hiltViewModel()
                    val isAuthorized by viewModel.isAuthorized.collectAsState()
                    isAuthorized?.let {
                        val startDestination = if (it) MainDirection.path else AuthDirection.path
                        val navController = rememberNavController()
                        Scaffold {
                            NavHost(
                                navController = navController,
                                startDestination = startDestination,
                            ) {
                                composable(MainDirection.path) { MainScreen(navController = navController) }
                                composable(AuthDirection.path) { AuthScreen(navController = navController) }
                                composable(RegisterDirection.path) { RegisterScreen(navController = navController) }
                                composable(CreateFighterDirection.path) {
                                    CreateFighterScreen(
                                        navController = navController,
                                        navigationState = CreateFighterViewModel.Create
                                    )
                                }
                                composable(
                                    UpdateFighterDirection.path + "?uid={uid}",
                                    arguments = listOf(navArgument("uid") {
                                        type = NavType.LongType
                                    })
                                ) {
                                    CreateFighterScreen(
                                        navController = navController,
                                        navigationState = CreateFighterViewModel.Update
                                    )
                                }
                                composable(
                                    ReadFighterDirection.path + "?uid={uid}",
                                    arguments = listOf(
                                        navArgument("uid") { type = NavType.LongType },
                                    )
                                ) {
                                    CreateFighterScreen(
                                        navController = navController,
                                        navigationState = CreateFighterViewModel.Read
                                    )
                                }
                                composable(
                                    ReadEventDirection.path + "?uid={uid}",
                                    arguments = listOf(
                                        navArgument("uid") { type = NavType.LongType },
                                    )
                                ) {
                                    EventScreen(navController = navController)
                                }
                                composable(
                                    CreateEventDirection.path,
                                ) {
                                    CreateEventScreen(
                                        navController = navController,
                                        navigationState = CreateEventViewModel.Create
                                    )
                                }
                                composable(
                                    UpdateEventDirection.path + "?uid={uid}",
                                    arguments = listOf(
                                        navArgument("uid") { type = NavType.LongType },
                                    )
                                ) {
                                    CreateEventScreen(
                                        navController = navController,
                                        navigationState = CreateEventViewModel.Update
                                    )
                                }
                                composable(
                                    UpdateEventFighterDirection.path + "?uid={uid}",
                                    arguments = listOf(
                                        navArgument("uid") { type = NavType.LongType },
                                    )
                                ) {
                                    UpdateEventFighterScreen(navController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}