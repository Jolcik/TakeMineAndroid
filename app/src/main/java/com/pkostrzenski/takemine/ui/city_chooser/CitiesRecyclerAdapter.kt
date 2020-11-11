package com.pkostrzenski.takemine.ui.city_chooser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.City
import kotlinx.android.synthetic.main.city_recycler_item.view.*

class CitiesRecyclerAdapter(
    var cities: List<City>,
    private val clickListener: (City) -> Unit
): RecyclerView.Adapter<CitiesRecyclerAdapter.CitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.city_recycler_item, parent, false)
        )
    }

    override fun getItemCount(): Int = cities.size

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bindCity(cities[position], clickListener)
    }


    class CitiesViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bindCity(city: City, clickListener: (City) -> Unit){
            view.cityItemName.text = city.name
            view.cityItemLayout.setOnClickListener {
                clickListener(city)
            }
        }
    }
}