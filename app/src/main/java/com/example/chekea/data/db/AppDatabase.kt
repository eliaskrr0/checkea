package com.example.chekea.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodItem::class], version = 1, exportSchema = false) // Define entidades y versión
abstract class AppDatabase : RoomDatabase() {

	abstract fun foodDAO(): FoodDAO // Room implementará esto

	companion object {
		@Volatile // Asegura que el valor de INSTANCE sea siempre actualizado y visible para todos los hilos
		private var INSTANCE: AppDatabase? = null

		fun getDatabase(context: Context): AppDatabase {
			// Si INSTANCE no es nulo, devuélvelo,
			// si es nulo, entonces crea la base de datos
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"food_database" // Nombre del archivo de la base de datos
				)
					.fallbackToDestructiveMigration() // Estrategia de migración simple para desarrollo
					.build()
				INSTANCE = instance
				// return instance
				instance
			}
		}
	}
}