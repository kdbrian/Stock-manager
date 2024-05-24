package io.github.junrdev.stockmanager.ui.screen.products

import android.os.Bundle
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

class ViewCategoryItems : Fragment() {

    lateinit var productsRecycler: RecyclerView
    lateinit var productsPreviewRecyclerAdapter: ProductsPreviewRecyclerAdapter
    lateinit var productRepository: ProductRepository
    lateinit var cat: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_category_items, container, false)

        view.apply {
            productsRecycler = findViewById(R.id.productsRecycler)
            cat = findViewById(R.id.cat)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = arguments?.getString("cat")
        productRepository = ProductRepository(requireContext())

        cat.setText(category)

        CoroutineScope(Dispatchers.IO).launch {
            val products = productRepository.getProductsWithCategory(category!!)

            withContext(Dispatchers.Main){
                productsPreviewRecyclerAdapter =
                    ProductsPreviewRecyclerAdapter(requireContext(), products){ pid ->
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
                productsRecycler.adapter = productsPreviewRecyclerAdapter
            }
        }

    }

    companion object {

        private const val TAG = "ViewCategoryItems"

    }

}