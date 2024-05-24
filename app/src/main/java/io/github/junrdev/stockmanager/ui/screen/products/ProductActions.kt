package io.github.junrdev.stockmanager.ui.screen.products

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.junrdev.stockmanager.R

class ProductActions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_actions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (intent.hasExtra("action")) {

            if (intent.getStringExtra("action").equals("add")) {

                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.productActionsFrameLayout, AddProduct(), "AddProduct")
                }.commit()

            } else if (intent.getStringExtra("action").equals("view")) {

                if (intent.hasExtra("cat") && !intent.getStringExtra("cat").equals("all")) {
                    val category = intent.getStringExtra("cat")
                    Log.d(TAG, "onCreate: $category")
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.productActionsFrameLayout, ViewCategoryItems().apply {
                            arguments = Bundle().apply {
                                putString("cat", category)
                            }
                        }, "AddProduct")
                    }.commit()
                } else if (intent.hasExtra("cat") && intent.getStringExtra("cat").equals("all")) {

                    supportFragmentManager.beginTransaction().apply {
                        replace(
                            R.id.productActionsFrameLayout,
                            ViewAllProducts(),
                            "ViewAllProducts"
                        )
                    }.commit()
                }

            } else if (intent.getStringExtra("action").equals("stockout")) {
                Log.d(TAG, "onCreate: stock out")
                supportFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.productActionsFrameLayout,
                        StockOut(),
                        "StockOut"
                    )
                }.commit()
            } else if (intent.getStringExtra("action").equals("edit")) {
                val pname = intent.getStringExtra("pname")

                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.productActionsFrameLayout, ViewSingleProduct().apply {
                            arguments = Bundle().apply {
                                putString("pname", pname)
                            }
                        }, "ViewSingleProduct")
                        addToBackStack("ViewSingleProduct")
                    }.commit()
            }
        }
    }

    companion object {
        private const val TAG = "ProductActions"
    }
}