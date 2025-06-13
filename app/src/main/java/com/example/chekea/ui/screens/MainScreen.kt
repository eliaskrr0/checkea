package com.example.chekea.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chekea.navigation.AppDestinations
import com.example.chekea.ui.theme.ChekeaTheme

@Composable
fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
	Column (
		modifier = modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Button(onClick = {
			navController.navigate(AppDestinations.ADD_FOOD_SCREEN)
		}) {
			Text("Agregar alimento")
		}

		Spacer(modifier = Modifier.height(16.dp))

		Button(onClick = {
			navController.navigate(AppDestinations.LIST_FOOD_SCREEN)
		}) {
			Text("Mostrar alimentos")
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
	ChekeaTheme {
		MainScreen(navController = rememberNavController())
	}
}