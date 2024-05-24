package io.github.junrdev.stockmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.stockmanager.adapter.ProductCategoriesRecyclerAdapter
import io.github.junrdev.stockmanager.adapter.StockAlertRecyclerAdapter
import io.github.junrdev.stockmanager.data.local.ProductRepository
import io.github.junrdev.stockmanager.model.CategoryItem
import io.github.junrdev.stockmanager.model.Product
import io.github.junrdev.stockmanager.model.StockAlerts
import io.github.junrdev.stockmanager.ui.screen.auth.Settings
import io.github.junrdev.stockmanager.ui.screen.products.ProductActions
import io.github.junrdev.stockmanager.ui.screen.products.ViewSingleProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var addProduct: CardView
    lateinit var viewAllProducts: CardView
    lateinit var stockOut: CardView
    lateinit var categoriesRecycler: RecyclerView
    lateinit var stockAlertsRecycler: RecyclerView
    lateinit var productCategoriesRecyclerAdapter: ProductCategoriesRecyclerAdapter
    lateinit var productRepository: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestFilePermissions()
        toolbar = findViewById(R.id.toolbar)
        categoriesRecycler = findViewById(R.id.categoriesRecycler)
        stockAlertsRecycler = findViewById(R.id.stockAlertsRecycler)
        setSupportActionBar(toolbar)
        addProduct = findViewById(R.id.addProduct)
        viewAllProducts = findViewById(R.id.viewAllProducts)
        stockOut = findViewById(R.id.stockOut)
        productRepository = ProductRepository(applicationContext)

        val productactionsintent = Intent(this, ProductActions::class.java)

        fetchItems()

        addProduct.setOnClickListener {
            productactionsintent.putExtra("action", "add")
            startActivity(productactionsintent)
        }

        viewAllProducts.setOnClickListener {
            productactionsintent.putExtra("action", "view")
            productactionsintent.putExtra("cat", "all")
            startActivity(productactionsintent)
        }

        stockOut.setOnClickListener {
            productactionsintent.putExtra("action", "stockout")
            startActivity(productactionsintent)
        }
    }

    private fun fetchItems() {
        val productactionsintent = Intent(this, ProductActions::class.java)
        categoriesRecycler.adapter = null
        stockAlertsRecycler.adapter = null

        CoroutineScope(Dispatchers.IO).launch {
            val products = productRepository.getProducts()
            val alerts = products.filter { product: Product -> product.stockCount < 10 }
                .map { StockAlerts(productName = it.productName, remaainingUnits = it.stockCount) }

            withContext(Dispatchers.Main) {
                val cats = products.flatMap { it.category }.distinct().map { CategoryItem(it) }

                productCategoriesRecyclerAdapter = ProductCategoriesRecyclerAdapter(
                    items = cats,
                    onItemClick = { category, position ->
                        productactionsintent.putExtra("action", "view")
                        productactionsintent.putExtra("cat", category)
                        startActivity(productactionsintent)
                    })

                stockAlertsRecycler.adapter = StockAlertRecyclerAdapter(alerts){
                    productactionsintent.apply {
                        putExtra("action", "edit")
                        putExtra("pname", it)
                    }

                    startActivity(productactionsintent)
                }

                categoriesRecycler.adapter = productCategoriesRecyclerAdapter

            }
        }
    }

    private fun requestFilePermissions() {
        if ((ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ), Constants.FILE_PICK_CODE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.homemenu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accountcenter -> {
                startActivity(Intent(this, Settings::class.java))
            }
        }


        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }
}