package io.github.junrdev.stockmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.junrdev.stockmanager.R
import io.github.junrdev.stockmanager.model.StockAlerts

class StockAlertRecyclerAdapter(
    val alerts: List<StockAlerts>,
    val onItemCick: ((pname : String) -> Unit)? = null
) : RecyclerView.Adapter<StockAlertRecyclerAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val alertProductName: TextView = itemView.findViewById(R.id.alertProductName)
        private val alertText: TextView = itemView.findViewById(R.id.alertText)
        private val alertItem: LinearLayout = itemView.findViewById(R.id.alertItem)

        fun bindItem(stockAlerts: StockAlerts) {
            alertProductName.setText(stockAlerts.productName)
            alertItem.setOnClickListener {
                onItemCick?.invoke(stockAlerts.productName)
            }
            alertText.setText(
                when (stockAlerts.remaainingUnits <= 0) {
                    true -> "product out of stock"
                    else -> "${stockAlerts.remaainingUnits} remaining"
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holderView =
            LayoutInflater.from(parent.context).inflate(R.layout.stockalertitem, parent, false)
        return VH(holderView)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val alert = alerts[position]
        holder.bindItem(alert)
    }
}