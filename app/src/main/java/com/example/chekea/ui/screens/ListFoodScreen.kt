package com.example.chekea.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chekea.data.db.FoodItem
import com.example.chekea.ui.global.Strings.TABLE_COLUMN_ACTIONS
import com.example.chekea.ui.global.Strings.BUTTON_BACK
import com.example.chekea.ui.global.Strings.BUTTON_CANCEL
import com.example.chekea.ui.global.Strings.BUTTON_DELETE_FOOD
import com.example.chekea.ui.global.Strings.BUTTON_EDIT_FOOD
import com.example.chekea.ui.global.Strings.TEXT_NAME
import com.example.chekea.ui.global.Strings.MESSAGE_NO_ITEMS
import com.example.chekea.ui.global.Strings.OPTION_DELETE
import com.example.chekea.ui.global.Strings.OPTION_EDIT
import com.example.chekea.ui.global.Strings.BUTTON_SAVE
import com.example.chekea.ui.global.Strings.TEXT_NOT_FOUND
import com.example.chekea.ui.theme.ChekeaTheme
import com.example.chekea.ui.viewmodel.FoodItemViewModel
import com.example.chekea.ui.viewmodel.FoodListUIState

val foodTableHeaders = listOf(TEXT_NAME, TABLE_COLUMN_ACTIONS)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListFoodScreen(
	navController: NavController,
	modifier: Modifier = Modifier,
	foodItemViewModel: FoodItemViewModel = viewModel()
) {
	val uiState by foodItemViewModel.foodListUIState.collectAsState()

	var itemToEdit by remember { mutableStateOf<FoodItem?>(null) }
	var itemToDelete by remember { mutableStateOf<FoodItem?>(null) }
	var editText by remember { mutableStateOf("") }

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Lista de Alimentos") },
				// Opción para volver al menú anterior
				navigationIcon = {
					if (navController.previousBackStackEntry != null) {
						IconButton(onClick = { navController.popBackStack() }) {
							Icon(
								imageVector = Icons.AutoMirrored.Filled.ArrowBack,
								contentDescription = BUTTON_BACK
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
					Text(MESSAGE_NO_ITEMS)
				}
			} else {
				// Muestra la tabla de alimentos
				FoodDataTable(
					headers = foodTableHeaders,
					foodItems = uiState.itemList,
					onEdit = { food ->
						itemToEdit = food
						editText = food.name
					},
					onDelete = { food ->
						itemToDelete = food
					}
				)
			}
		}

		// Opción para editar registro
		itemToEdit?.let { item ->
			AlertDialog(
				onDismissRequest = { itemToEdit = null},
				title = { Text(BUTTON_EDIT_FOOD) },
				text = {
					OutlinedTextField(
						value = editText,
						onValueChange = { editText = it },
						label = { Text(TEXT_NAME) }
					)
				},
				confirmButton = {
					TextButton(onClick = {
						foodItemViewModel.update(item.copy(name = editText))
						itemToEdit = null
					}) {
						Text(BUTTON_SAVE)
					}
				},
				dismissButton = {
					TextButton(onClick = { itemToEdit = null }) {
						Text(BUTTON_CANCEL)
					}
				}
			)

		}

		// Opción para eliminar
		itemToDelete?.let { item ->
			AlertDialog(
				onDismissRequest = { itemToDelete = null },
				title = { Text(BUTTON_DELETE_FOOD) },
				text = { Text("¿Estás seguro de eliminar el alimento ${item.name}") },
				confirmButton = {
					TextButton(onClick = {
						foodItemViewModel.delete(item)
						itemToDelete = null
					}) {
						Text(OPTION_DELETE)
					}
				},
				dismissButton = {
					TextButton(onClick = { itemToDelete = null }) {
						Text(BUTTON_CANCEL)
					}
				}
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodDataTable(
	headers: List<String>,
	foodItems: List<FoodItem>,
	onEdit: (FoodItem) -> Unit,
	onDelete: (FoodItem) -> Unit,
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

		// Filas de datos
		items(foodItems, key = { foodItem -> foodItem.id ?: foodItem.name }) { foodItem -> // Usa una clave única
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 4.dp, horizontal = 8.dp)
					.height(IntrinsicSize.Min) // Asegura que todas las celdas en la fila tengan la misma altura
			) {
				// Muestra todos los registros
				headers.forEach { header ->
					// Muestra los iconos para editar/eliminar un registro
					if (header == TABLE_COLUMN_ACTIONS) {
						Row(modifier = Modifier.weight(1f)) {
							// Editar
							IconButton(onClick = { onEdit(foodItem) }) {
								Icon(imageVector = Icons.Filled.Edit, contentDescription = OPTION_EDIT)
							}
							// Eliminar
							IconButton(onClick = { onDelete(foodItem) }) {
								Icon(imageVector = Icons.Filled.Delete, contentDescription = OPTION_DELETE)
							}
						}
					} else {
						val cellText = when (header) {
							TEXT_NAME -> foodItem.name
							else -> TEXT_NOT_FOUND
						}
						FoodTableCell(text = cellText, weight = 1f / headers.size)
					}
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
	modifier: Modifier = Modifier,
	onEdit: (FoodItem) -> Unit = {},
	onDelete: (FoodItem) -> Unit = {}
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
				Text(MESSAGE_NO_ITEMS)
			}
		} else {
			FoodDataTable(
				headers = headers,
				foodItems = uiState.itemList,
				onEdit = onEdit,
				onDelete = onDelete
			)
		}
	}
}