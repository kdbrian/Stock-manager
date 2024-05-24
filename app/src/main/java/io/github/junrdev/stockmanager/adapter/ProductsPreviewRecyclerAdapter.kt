package io.github.junrdev.stockmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.model.CategoryItem
import io.github.junrdev.stockmanager.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsPreviewRecyclerAdapter(
    val context: Context,
    val products: List<Product>,
    val onProductSelected: ((pid: Long) -> Unit)?=null
) : RecyclerView.Adapter<ProductsPreviewRecyclerAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productPreview: ImageView = itemView.findViewById(R.id.productPreview)
        private val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        private val productStock: TextView = itemView.findViewById(R.id.productStock)
        private val productItem: LinearLayout = itemView.findViewById(R.id.productItem)
        private val categoriesRecycler: RecyclerView =
            itemView.findViewById(R.id.categoriesRecycler)

        fun bindItem(item: Product) {
            item.apply {

                image?.let {
                    Glide.with(context)
                        .load(it)
                        .into(productPreview)
                }

                productName?.let {
                    productTitle.setText(it)
                }

                if (stockCount == null) {
                    productStock.setText("item out of stock")
                }

                stockCount.let {
                    productStock.setText("$it units")
                }

                categoriesRecycler.adapter = AddProductCategoryRecyclerAdapter(
                    categories = category.map { CategoryItem(it) }.toMutableList(),
                    false
                )

                productItem.setOnClickListener {
                    onProductSelected?.invoke(pid)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val holderview =
            LayoutInflater.from(parent.context).inflate(R.layout.productitempreview, parent, false)
        return VH(holderview)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = products[position]
        holder.bindItem(item)
    }
}