package com.example.chekea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chekea.navigation.AppDestinations
import com.example.chekea.ui.screens.AddFoodScreen
import com.example.chekea.ui.screens.ListFoodScreen
import com.example.chekea.ui.screens.MainScreen
import com.example.chekea.ui.theme.ChekeaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChekeaTheme { // Un solo ChekeaTheme aquí es suficiente
                // El NavController se crea aquí para que AppNavigation lo reciba
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pasamos el navController y el innerPadding a AppNavigation
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding) // Aplicamos el padding del Scaffold principal
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    // NavHost ahora usa el modifier que contiene el padding del Scaffold de MainActivity
    NavHost(
        navController = navController,
        startDestination = AppDestinations.MAIN_SCREEN,
        modifier = modifier // Aplicar el modifier aquí
    ) {
        composable(AppDestinations.MAIN_SCREEN) {
            MainScreen(navController = navController)
        }
        composable(AppDestinations.ADD_FOOD_SCREEN) {
            AddFoodScreen(navController = navController)
        }
        composable(AppDestinations.LIST_FOOD_SCREEN) {
            ListFoodScreen(navController = navController)
        }
    }
}