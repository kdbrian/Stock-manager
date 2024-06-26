package io.github.junrdev.stockmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import io.github.junrdev.stockmanager.model.Converters
import io.github.junrdev.stockmanager.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {

    abstract fun productsDao(): ProductsDao

    companion object {
        @Volatile
        var INSTANCE: AppDB? = null

        fun getAppDb(context: Context): AppDB {
            return INSTANCE ?: synchronized(this) {
                buildDatabase(context).also { INSTANCE = it }
            }
        }

        fun buildDatabase(context: Context): AppDB =
            Room.databaseBuilder(context, AppDB::class.java, "stockmanagementdb")
//                .addMigrations(
//                    Migration(2, 1) {}
//                )
                .build()
    }
}