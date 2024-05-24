package io.github.junrdev.stockmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.model.Product

class StockOutChosenItemRecyclerAdapter(
    val context: Context,
    val items: MutableList<Product> = mutableListOf()
) : RecyclerView.Adapter<StockOutChosenItemRecyclerAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        private val itemsCountSpinner: Spinner = itemView.findViewById(R.id.itemsCountSpinner)
        private val stockItemPrev: ImageView = itemView.findViewById(R.id.stockItemPrev)


        fun bindItem(product: Product) {
            itemTitle.setText(product.productName)
            itemsCountSpinner.apply {
                adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, (1 .. product.stockCount).toList())
            }

            Glide.with(context)
                .load(product.image)
                .into(stockItemPrev)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holderView =
            LayoutInflater.from(parent.context).inflate(R.layout.stockoutsingleitem, parent, false)
        return VH(holderView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindItem(items[position])
    }
}