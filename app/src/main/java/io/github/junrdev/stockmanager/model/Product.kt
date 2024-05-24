package io.github.junrdev.stockmanager.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    var pid: Long = 0L,
    var productName: String,
    var category: List<String>,
    var price: Double = 0.0,
    var image: String,
//    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var dateAdded: String = LocalDateTime.now().toString(),
    var stockCount: Long = 0L
)