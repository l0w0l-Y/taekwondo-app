package com.taekwondo.taekwondoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taekwondo.corenavigation.AuthDirection
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.corenavigation.RegisterDirection
import com.taekwondo.coretheme.AppTheme
import com.taekwondo.featureauth.presentation.auth.AuthScreen
import com.taekwondo.featureauth.presentation.register.RegisterScreen
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
                    //TODO: Заменить на реальную логику авторизации
                    val isAuthorized = false
                    val startDestination =
                        if (isAuthorized) MainDirection.path else AuthDirection.path
                    val navController = rememberNavController()
                    Scaffold {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                        ) {
                            composable(MainDirection.path) { AuthScreen(navController = navController) }
                            composable(AuthDirection.path) { AuthScreen(navController = navController) }
                            composable(RegisterDirection.path) { RegisterScreen(navController = navController) }
                        }
                    }
                }
            }
        }
    }
}