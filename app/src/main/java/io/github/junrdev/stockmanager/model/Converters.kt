package io.github.junrdev.stockmanager.model

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listtype = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listtype)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

//    @TypeConverter
//    fun fromStringUri(value: String): Uri {
//        return Gson().fromJson(value, object : TypeToken<Uri>() {}.type)
//    }
//
//    @TypeConverter
//    fun fromUri(uri: Uri) : String{
//        return Gson().toJson(uri)
//    }

}