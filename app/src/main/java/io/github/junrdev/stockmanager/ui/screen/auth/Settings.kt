package io.github.junrdev.stockmanager.ui.screen.auth

import android.content.Context
import android.os.Bundle
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.junrdev.stockmanager.Constants
import io.github.junrdev.stockmanager.R

class Settings : AppCompatActivity() {

    lateinit var multipleStores: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        multipleStores = findViewById(R.id.multipleStores)
        val sharedPreferences =
            getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE).apply {
                multipleStores.isChecked = getBoolean(Constants.MULTIPLE_STORES, false)
            }

//        multipleStores.isChecked = sharedPreferences.getBoolean(Constants.MULTIPLE_STORES, false)

        multipleStores.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().apply {
                putBoolean(Constants.MULTIPLE_STORES, isChecked)
            }.apply()
        }


    }
}