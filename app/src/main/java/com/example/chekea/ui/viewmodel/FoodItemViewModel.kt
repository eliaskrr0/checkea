package com.example.chekea.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chekea.data.db.AppDatabase
import com.example.chekea.data.db.FoodItem
import com.example.chekea.data.repository.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FoodListUIState (
	val itemList: List<FoodItem> = listOf(),
	val isLoading: Boolean = false
)

class FoodItemViewModel(application: Application) : AndroidViewModel(application) {

	private val repository: FoodRepository
	val foodListUIState: StateFlow<FoodListUIState>

	init {
		val foodDAO = AppDatabase.getDatabase(application).foodDAO()
		repository = FoodRepository(foodDAO)

		foodListUIState = repository.allFoodItemsStream
			.map { items -> FoodListUIState(itemList = items, isLoading = false) }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5000L),
				initialValue = FoodListUIState(isLoading = true)
			)
	}

	/**
	 * Lanza una nueva corrutina para insertar datos de forma no bloqueante
	 */
	fun insert(foodName: String) = viewModelScope.launch(Dispatchers.IO) {
		if (foodName.isNotBlank()) {
			val newFood = FoodItem(name = foodName)
			repository.insert(newFood)
		}
	}

	fun update(foodItem: FoodItem) = viewModelScope.launch(Dispatchers.IO) {
		repository.updateFood(foodItem)
	}

	fun delete(foodItem: FoodItem) = viewModelScope.launch(Dispatchers.IO) {
		repository.deleteFood(foodItem)
	}
}