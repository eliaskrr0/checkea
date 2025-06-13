package com.example.chekea.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chekea.data.db.FoodItem
import com.example.chekea.ui.theme.ChekeaTheme
import com.example.chekea.ui.viewmodel.FoodItemViewModel
import com.example.chekea.ui.viewmodel.FoodListUIState

val foodTableHeaders = listOf("Nombre del alimento")
// Si añades más atributos a FoodItem, añádelos aquí, ej: "Categoría", "Fecha de Caducidad"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListFoodScreen(
	navController: NavController,
	modifier: Modifier = Modifier,
	foodItemViewModel: FoodItemViewModel = viewModel()
) {
	val uiState by foodItemViewModel.foodListUIState.collectAsState()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Lista de Alimentos") },
				navigationIcon = {
					if (navController.previousBackStackEntry != null) {
						IconButton(onClick = { navController.popBackStack() }) {
							Icon(
								imageVector = Icons.AutoMirrored.Filled.ArrowBack,
								contentDescription = "Volver atrás"
							)
						}
					}
				}
			)
		}
	) { innerPadding ->
		Column(
			modifier = modifier
				.padding(innerPadding)
				.padding(horizontal = 8.dp)
				.fillMaxSize()
		) {
			if (uiState.isLoading) {
				// Muestra un indicador de carga mientras los datos se obtienen
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					CircularProgressIndicator()
				}
			} else if (uiState.itemList.isEmpty()) {
				// Muestra un mensaje si no hay alimentos
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					Text("No hay alimentos guardados.")
				}
			} else {
				// Muestra la tabla de alimentos
				FoodDataTable(
					headers = foodTableHeaders,
					foodItems = uiState.itemList
				)
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDataTable(
	headers: List<String>,
	foodItems: List<FoodItem>,
	modifier: Modifier = Modifier
) {
	LazyColumn(
		modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant)
	) {
		// Cabecera Fija
		stickyHeader {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(MaterialTheme.colorScheme.surfaceVariant) // Un color sutil para la cabecera
					.padding(vertical = 8.dp, horizontal = 8.dp)
			) {
				headers.forEach { headerText ->
					FoodTableCell(
						text = headerText,
						weight = 1f / headers.size, // Todas las columnas con el mismo ancho por ahora
						isHeader = true
					)
				}
			}
		}

		// Filas de Datos
		items(foodItems, key = { foodItem -> foodItem.id ?: foodItem.name }) { foodItem -> // Usa una clave única
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 4.dp, horizontal = 8.dp)
					.height(IntrinsicSize.Min) // Asegura que todas las celdas en la fila tengan la misma altura
			) {
				// Itera sobre las cabeceras para determinar qué mostrar y en qué orden
				headers.forEach { header ->
					val cellText = when (header) {
						"Nombre del Alimento" -> foodItem.name
						// Añade más casos aquí cuando tengas más atributos
						// "Categoría" -> foodItem.category ?: "N/A"
						// "Fecha de Caducidad" -> foodItem.expiryDate?.toString() ?: "N/A"
						else -> "N/D" // No Definido
					}
					FoodTableCell(text = cellText, weight = 1f / headers.size)
				}
			}
			Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp) // Divisor entre filas
		}
	}
}

@Composable
fun RowScope.FoodTableCell(
	text: String,
	weight: Float,
	isHeader: Boolean = false,
	alignment: TextAlign = TextAlign.Start // Por defecto alineado a la izquierda
) {
	Text(
		text = text,
		modifier = Modifier
			.weight(weight)
			.padding(vertical = 8.dp, horizontal = 4.dp), // Padding interno de la celda
		fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
		textAlign = alignment,
		color = if (isHeader) MaterialTheme.colorScheme.onSurfaceVariant else LocalContentColor.current,
		style = if (isHeader) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium
	)
}


@Preview(showBackground = true)
@Composable
fun ListFoodScreenPreview_Empty() {
	ChekeaTheme {
		// Simula el estado de UI para la vista previa
		val previewUiState = FoodListUIState(itemList = emptyList(), isLoading = false)
		// Para la vista previa, el NavController puede ser uno dummy
		ListFoodScreenContent(uiState = previewUiState, headers = foodTableHeaders)
	}
}

@Preview(showBackground = true)
@Composable
fun ListFoodScreenPreview_WithData() {
	ChekeaTheme {
		val sampleItems = listOf(
			FoodItem(id = 1, name = "Manzana"),
			FoodItem(id = 2, name = "Leche Descremada"),
			FoodItem(id = 3, name = "Pan Integral de Centeno"),
			FoodItem(id = 4, name = "Yogur Griego Natural")
		)
		val previewUiState = FoodListUIState(itemList = sampleItems, isLoading = false)
		ListFoodScreenContent(uiState = previewUiState, headers = foodTableHeaders)
	}
}

// Composable de contenido separado para facilitar las vistas previas sin NavController completo
@Composable
fun ListFoodScreenContent(
	uiState: FoodListUIState,
	headers: List<String>,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.padding(horizontal = 8.dp)
			.fillMaxSize()
	) {
		if (uiState.isLoading) {
			Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				CircularProgressIndicator()
			}
		} else if (uiState.itemList.isEmpty()) {
			Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				Text("No hay alimentos guardados.")
			}
		} else {
			FoodDataTable(
				headers = headers,
				foodItems = uiState.itemList
			)
		}
	}
}