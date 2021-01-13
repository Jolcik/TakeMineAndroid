package com.pkostrzenski.takemine.ui.product

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Picture

class PhotoRecyclerAdapter (
    private val pictures: List<Picture>,
    private val context: Context): RecyclerView.Adapter<PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.product_lower_image_recycler_item, parent, false)
        return PhotoViewHolder(
            inflatedView
        )
    }

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindGroup(pictures[position], context)
    }

}