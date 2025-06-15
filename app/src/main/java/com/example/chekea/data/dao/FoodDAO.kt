package com.example.chekea.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDAO {

	@Insert(onConflict = OnConflictStrategy.IGNORE) // Si hay conflicto de ID, ignora la inserción
	suspend fun insertFood(foodItem: FoodItem) // `suspend` para operaciones asíncronas

	@Query("SELECT * FROM food_items ORDER BY name ASC")
	fun getAllFoodItems(): Flow<List<FoodItem>> // Flow para observar cambios en los datos

	@Query("SELECT * FROM food_items WHERE name = :name")
	suspend fun getFoodItemByName(name: String): FoodItem?

	@Update	suspend fun updateFood(foodItem: FoodItem)

	@Delete	suspend fun deleteFood(foodItem: FoodItem)

}