package io.github.junrdev.stockmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.model.CategoryItem

class ProductCategoriesRecyclerAdapter(
    val onItemClick: ((category: String, position: Int) -> Unit)? = null,
    val items: List<CategoryItem> = listOf()
) : RecyclerView.Adapter<ProductCategoriesRecyclerAdapter.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holderView =
            LayoutInflater.from(parent.context).inflate(R.layout.categoryitem, parent, false)
        return VH(holderView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.bindItem(item, onItemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        private val categoryItemCount: TextView = itemView.findViewById(R.id.categoryItemCount)
        private val selectCategory: CardView = itemView.findViewById(R.id.selectCategory)


        fun bindItem(
            categoryItem: CategoryItem,
            onItemClick: ((cat: String, position: Int) -> Unit)? = null
        ) {
            categoryName.setText(categoryItem.text)

            selectCategory.setOnClickListener {
                Log.d(TAG, "bindItem: clicked")
                onItemClick?.invoke(categoryItem.text, adapterPosition)
            }

            categoryItem.itemcount?.let {
                categoryItemCount.setText("$it units.")
            }
        }
    }

    companion object{
        private const val TAG = "ProductCategoriesRecycl"
    }
}