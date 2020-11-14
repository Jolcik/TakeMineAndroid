package com.pkostrzenski.takemine.ui.main.fragments.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Product

class ProductRecyclerAdapter (
    private val products: List<Product>,
    private val context: Context,
    private val clickListener: (Int) -> Unit): RecyclerView.Adapter<ProductViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.product_recycler_item, parent, false)
            return ProductViewHolder(
                inflatedView
            )
        }

        override fun getItemCount(): Int = products.size

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bindGroup(products[position], clickListener, context)
        }

    }