package com.pkostrzenski.takemine.ui.main.fragments.main

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pkostrzenski.takemine.api.ApiFactory
import com.pkostrzenski.takemine.models.Product
import kotlinx.android.synthetic.main.product_recycler_item.view.*

class ProductViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private var view: View = v

    private val baseUrl: String = ApiFactory.URL

    fun bindGroup(product: Product, clickListener: (Int) -> Unit, context: Context){
        view.apply {
            productTitle.text = product.name
            if (product.locations.isNotEmpty())
                addressTitle.text = product.locations.first().name
            mainCardview.setOnClickListener{
                clickListener(product.id!!)
            }
        }

        if (product.pictures.isNotEmpty())
            Glide.with(context)
                .load("${baseUrl}/api/pictures/${product.pictures.first().path}")
                .into(view.productImage)
        else view.mainCardview.noPhotosText.visibility = View.VISIBLE
    }
}