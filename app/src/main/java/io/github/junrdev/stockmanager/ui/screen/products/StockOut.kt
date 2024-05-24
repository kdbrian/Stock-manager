package io.github.junrdev.stockmanager.ui.screen.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.adapter.ProductsPreviewRecyclerAdapter
import io.github.junrdev.stockmanager.adapter.StockOutChosenItemRecyclerAdapter
import io.github.junrdev.stockmanager.data.local.ProductRepository
import io.github.junrdev.stockmanager.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockOut : Fragment(), SearchView.OnQueryTextListener {

    lateinit var searchListItems: RecyclerView
    lateinit var chosenItems: RecyclerView
    lateinit var searchQuery: SearchView
    lateinit var products: List<Product>
    var selectedProducts: MutableList<Product> = mutableListOf()
    lateinit var productRepository: ProductRepository
    lateinit var productsPreviewRecyclerAdapter: ProductsPreviewRecyclerAdapter
    lateinit var stockOutProceed: CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stock_out, container, false)

        view.apply {
            searchQuery = findViewById(R.id.searchQuery)
            chosenItems = findViewById(R.id.chosenItems)
            searchListItems = findViewById(R.id.searchListItems)
            stockOutProceed = findViewById(R.id.stockOutProceed)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRepository = ProductRepository(requireContext())
        searchQuery.setOnQueryTextListener(this)
        chosenItems.adapter =
            StockOutChosenItemRecyclerAdapter(requireContext(), items = selectedProducts)


        CoroutineScope(Dispatchers.Main).launch {
            products = productRepository.getProducts()
            Log.d(TAG, "onViewCreated: $products")
            Log.d(TAG, "onViewCreated: all ${products.size}")
        }

        stockOutProceed.setOnClickListener {
            //proceed to manage stock
            proceedToStockOut()
        }
    }

    private fun proceedToStockOut() {
        for (product in selectedProducts) {

        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (products.isNotEmpty() && query != null) {
            searchListItems.adapter = null
            getSearchedItems(query)
        }
        return true
    }

    private fun getSearchedItems(query: String) {
        requireActivity().runOnUiThread {
            val filtered =
                products.filter { (it.productName.contains(query) || it.category.contains(query)) && it.stockCount > 0 }
            Log.d(TAG, "getSearchedItems: $filtered")
            Log.d(TAG, "getSearchedItems: filtered ${filtered.size}")
            productsPreviewRecyclerAdapter = ProductsPreviewRecyclerAdapter(
                requireContext(),
                filtered
            ) { pid ->
                val prod = products.first { it.pid == pid }
                if (!selectedProducts.contains(prod))
                    selectedProducts.add(prod)
                searchListItems.adapter = null
            }
            searchListItems.adapter = productsPreviewRecyclerAdapter
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty() && products.isNotEmpty())
            getSearchedItems(newText)
        return true
    }

    companion object {
        private const val TAG = "StockOut"
    }
}