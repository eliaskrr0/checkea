package com.example.chekea.data.repository // O el paquete que elijas

import com.example.chekea.data.db.FoodDAO
import com.example.chekea.data.db.FoodItem
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodDAO: FoodDAO) {

	val allFoodItemsStream: Flow<List<FoodItem>> = foodDAO.getAllFoodItems()

	suspend fun insert(foodItem: FoodItem) {
		foodDAO.insertFood(foodItem)
	}

	suspend fun getFoodItemByName(name: String): FoodItem? {
		return foodDAO.getFoodItemByName(name);
	}
}