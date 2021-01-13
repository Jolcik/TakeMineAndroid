package com.pkostrzenski.takemine.ui.product

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pkostrzenski.takemine.api.ApiFactory
import com.pkostrzenski.takemine.models.Picture
import kotlinx.android.synthetic.main.product_lower_image_recycler_item.view.*

class PhotoViewHolder(v: View): RecyclerView.ViewHolder(v) {

    private var view: View = v

    private val baseUrl: String = ApiFactory.URL

    fun bindGroup(picture: Picture, context: Context){
        Glide.with(context)
            .load("${baseUrl}/api/pictures/${picture.path}")
            .into(view.productLowerImage)
    }
}