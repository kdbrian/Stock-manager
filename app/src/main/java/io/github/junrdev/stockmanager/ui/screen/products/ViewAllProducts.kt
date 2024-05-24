package io.github.junrdev.stockmanager.ui.screen.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.adapter.ProductsPreviewRecyclerAdapter
import io.github.junrdev.stockmanager.data.local.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewAllProducts : Fragment() {

    lateinit var allProductsRecycler: RecyclerView
    lateinit var productsPreviewRecyclerAdapter: ProductsPreviewRecyclerAdapter
    lateinit var productRepository: ProductRepository
    lateinit var productInStockCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_all_products, container, false)

        Log.d(TAG, "onCreateView: was created")

        view.apply {
            allProductsRecycler = findViewById(R.id.allProductsRecycler)
            productInStockCount = findViewById(R.id.productInStockCount)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRepository = ProductRepository(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val products = productRepository.getProducts()

            Log.d(TAG, "onViewCreated: $products")

            withContext(Dispatchers.Main) {
                productsPreviewRecyclerAdapter =
                    ProductsPreviewRecyclerAdapter(requireContext(), products) { pid ->
                        requireActivity().supportFragmentManager.beginTransaction()
                            .apply {
                                replace(R.id.productActionsFrameLayout, ViewSingleProduct().apply {
                                    arguments = Bundle().apply {
                                        putLong("pid", pid)
                                    }
                                }, "ViewSingleProduct")
                                addToBackStack("ViewSingleProduct")
                            }.commit()
                    }

                allProductsRecycler.adapter = productsPreviewRecyclerAdapter
                productInStockCount.setText("${products.size}")
            }
        }
    }

    companion object {
        private const val TAG = "ViewAllProducts"
    }
}