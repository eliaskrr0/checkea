package com.example.chekea.data.db // O el paquete que elijas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items") // Define el nombre de la tabla
data class FoodItem(
	@PrimaryKey(autoGenerate = true) // Define la clave primaria, se autogenera
	val id: Int = 0,
	val name: String
)