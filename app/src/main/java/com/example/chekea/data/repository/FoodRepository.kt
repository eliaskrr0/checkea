package com.example.chekea.data.repository

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

	suspend fun updateFood(foodItem: FoodItem) {
		foodDAO.updateFood(foodItem)
	}

	suspend fun deleteFood(foodItem: FoodItem) {
		foodDAO.deleteFood(foodItem)
	}
}