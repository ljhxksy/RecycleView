package com.example.recycleview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val orders = mutableListOf<Order>()
    private lateinit var adapter: OrderAdapter
    private var isLoading = false
    private var currentPage = 1
    private val pageSize = 20 // Number of orders per list
    private val threshold = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize recyclerView and swipeRefreshLayout
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        adapter = OrderAdapter(orders)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Setting up the swipe-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            if (!isLoading) {
                refreshOrders()
            }
        }

        // Add a scroll listener to the RecyclerView to implement load more orders
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Load orders if the recycler view is not currently loading and the total item count
                // is less than or equal to the last visible item position + the threshold (10)
                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + threshold)) {
                    loadMoreOrders()
                }
            }
        })

        // Load initial orders
        loadMoreOrders()
    }

    // Treating each load of orders as a "page", each time loadMoreOrders is called,
    // a new "page" of orders is loaded and added to the existing list in the RecyclerView

    // Is called when the user scrolls to the bottom of the list and more items need to be loaded
    private fun loadMoreOrders() {
        isLoading = true

        lifecycleScope.launch {
            delay(1500)
            val newOrders = generateOrders(currentPage, pageSize)
            adapter.addOrders(newOrders)
            currentPage++
            isLoading = false
        }
    }

    // Refresh the entire order list when user carries out a pull-to-refresh gesture
    private fun refreshOrders() {
        isLoading = true

        //Reset currentPage to 1 to start loading from the first page
        currentPage = 1

        // Simulate network delay
        lifecycleScope.launch {
            delay(1500)
            // Switch to the main thread to before performing UI updates
            withContext(Dispatchers.Main) {
                // Clear the orders list
                orders.clear()

                // Generate new orders
                val newOrders = generateOrders(currentPage, pageSize)

                // Update the adapter
                adapter.clearOrders()
                adapter.addOrders(newOrders)

                // Stop the refreshing animation
                swipeRefreshLayout.isRefreshing = false
                currentPage++
                isLoading = false
            }

        }
    }

    // Idk what else to put in for orders....
    private fun generateOrders(page: Int, size: Int): List<Order> {
        val orders = mutableListOf<Order>()
        val start = (page - 1) * size + 1
        val end = start + size - 1
        for (i in start..end) {
            orders.add(
                Order(
                    "Order $i",
                    "Details for order $i",
                    "Status $i"
                )
            )
        }
        return orders
    }
}





