package com.example.chekea.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chekea.ui.global.Strings.BUTTON_ADD_FOOD
import com.example.chekea.ui.global.Strings.BUTTON_BACK
import com.example.chekea.ui.global.Strings.TEXT_NAME
import com.example.chekea.ui.global.Strings.BUTTON_SAVE
import com.example.chekea.ui.theme.ChekeaTheme
import com.example.chekea.ui.viewmodel.FoodItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
	navController: NavController,
	modifier: Modifier = Modifier,
	foodItemViewModel: FoodItemViewModel = viewModel()
) {
	var foodName by remember { mutableStateOf("") }
	// Estado para controlar si se muestra un mensaje de error
	var showError by remember { mutableStateOf(false) }

	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier.fillMaxWidth(),
		) {
			// Botón para volver atrás
			IconButton(
				onClick = { navController.popBackStack() },
				modifier = Modifier.align(Alignment.CenterStart)
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Filled.ArrowBack,
					contentDescription = BUTTON_BACK
				)
			}
			Text(
				text = BUTTON_ADD_FOOD,
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.align(Alignment.Center) // Centra el título del Box
			)
		}


		Spacer(modifier = Modifier.height(24.dp))

		Column (
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			OutlinedTextField(
				value = foodName,
				onValueChange = {
					foodName = it
					if (showError) showError = false
				},
				label = { Text(TEXT_NAME) },
				singleLine = true,
				modifier = Modifier.fillMaxWidth(),
				isError = showError
			)
		}

		if (showError) {
			Text(
				text = "El nombre del alimento no puede estar vacío",
				color = MaterialTheme.colorScheme.error,
				style = MaterialTheme.typography.bodySmall,
				modifier = Modifier.padding(start = 16.dp, top = 4.dp)
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			onClick = {
				if (foodName.isNotBlank()) {
					foodItemViewModel.insert(foodName) // Llama al método insert del ViewModel
					println("Solicitando guardar: $foodName") // Puedes mantener esto para depuración
					navController.popBackStack()
				} else {
					showError = true
				}
			},
			modifier = Modifier.fillMaxWidth()
		) {
			Text(BUTTON_SAVE)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun AddFoodScreenPreview() {
	ChekeaTheme {
		// En la preview, el ViewModel se creará pero no interactuará realmente con la DB.
		AddFoodScreen(navController = rememberNavController())
	}
}

@Preview(showBackground = true, name = "AddFoodScreen With Error Preview")
@Composable
fun AddFoodScreenWithErrorPreview() {
	ChekeaTheme {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = BUTTON_ADD_FOOD,
				style = MaterialTheme.typography.headlineSmall
			)
			Spacer(modifier = Modifier.height(24.dp))

			var foodNamePreview by remember { mutableStateOf("") }
			var showErrorPreview by remember { mutableStateOf(true) }

			OutlinedTextField(
				value = foodNamePreview,
				onValueChange = { foodNamePreview = it },
				label = { Text(TEXT_NAME) },
				singleLine = true,
				modifier = Modifier.fillMaxWidth(),
				isError = showErrorPreview
			)
			if (showErrorPreview) {
				Text(
					text = "El nombre del alimento no puede estar vacío",
					color = MaterialTheme.colorScheme.error,
					style = MaterialTheme.typography.bodySmall,
					modifier = Modifier.padding(start = 16.dp, top = 4.dp)
				)
			}
			Spacer(modifier = Modifier.height(16.dp))
			Button(
				onClick = { /* No action */ },
				modifier = Modifier.fillMaxWidth()
			) {
				Text(BUTTON_SAVE)
			}
		}
	}
}