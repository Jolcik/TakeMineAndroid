package com.pkostrzenski.takemine.ui.main.fragments.main

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pkostrzenski.takemine.models.Product
import kotlinx.android.synthetic.main.product_recycler_item.view.*

class ProductViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private var view: View = v

    fun bindGroup(product: Product, clickListener: (Int) -> Unit, context: Context){
        view.apply {
            productTitle.text = product.name
            addressTitle.text = product.address
            main_cardview.setOnClickListener{
                clickListener(product.id)
            }
        }

        Glide.with(context)
            .load("https://picsum.photos/400/200") // sample image
            .placeholder(android.R.color.darker_gray)
            .into(view.productImage)
    }
}