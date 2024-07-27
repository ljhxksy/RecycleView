package com.example.recycleview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val orders = mutableListOf<Order>()
    private lateinit var adapter: OrderAdapter
    private var isLoading = false
    private var currentPage = 1
    private val pageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        adapter = OrderAdapter(orders)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            refreshOrders()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadMoreOrders()
                }
            }
        })

        loadMoreOrders()
    }

    private fun loadMoreOrders() {
        isLoading = true
        // Simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            val newOrders = generateFakeOrders(currentPage, pageSize)
            adapter.addOrders(newOrders)
            currentPage++
            isLoading = false
        }, 1500)
    }

    private fun refreshOrders() {
        isLoading = true
        currentPage = 1
        // Simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            orders.clear()
            val newOrders = generateFakeOrders(currentPage, pageSize)
            adapter.clearOrders()
            adapter.addOrders(newOrders)
            swipeRefreshLayout.isRefreshing = false
            isLoading = false
        }, 1500)
    }

    private fun generateFakeOrders(page: Int, size: Int): List<Order> {
        val fakeOrders = mutableListOf<Order>()
        val start = (page - 1) * size + 1
        val end = start + size - 1
        for (i in start..end) {
            fakeOrders.add(Order("Order $i", "Details for order $i", "Status $i"))
        }
        return fakeOrders
    }
}





