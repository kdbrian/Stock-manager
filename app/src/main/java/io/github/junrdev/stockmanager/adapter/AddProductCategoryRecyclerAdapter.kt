package io.github.junrdev.stockmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.model.CategoryItem

class AddProductCategoryRecyclerAdapter(
    val categories: MutableList<CategoryItem> = mutableListOf(),
    val removable: Boolean? = true
) :
    RecyclerView.Adapter<AddProductCategoryRecyclerAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holderView =
            LayoutInflater.from(parent.context).inflate(R.layout.categoriseitem, parent, false)
        return VH(holderView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = categories[position]
        holder.bindItem(item) {
            when (removable!!) {
                true -> {
                    categories.remove(item)
                    notifyItemRemoved(position)
                }

                else -> {
                    holder.itemView.findViewById<CardView>(R.id.removeCategory).visibility =
                        View.GONE
                }
            }
        }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        private val removeCategory: CardView = itemView.findViewById(R.id.removeCategory)

        fun bindItem(categoryItem: CategoryItem, onRemove: (() -> Unit)) {
            categoryName.setText(categoryItem.text)

            removeCategory.setOnClickListener {
                onRemove?.invoke()
            }
        }
    }
}