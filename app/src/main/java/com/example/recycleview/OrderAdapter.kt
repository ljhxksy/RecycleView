package com.example.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orders: MutableList<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.orderName)
        val details: TextView = itemView.findViewById(R.id.orderDetails)
        val status: TextView = itemView.findViewById(R.id.orderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.name.text = order.name
        holder.details.text = order.details
        holder.status.text = order.status
    }

    override fun getItemCount() = orders.size

    fun addOrders(newOrders: List<Order>) {
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    fun clearOrders() {
        orders.clear()
        notifyDataSetChanged()
    }
}
