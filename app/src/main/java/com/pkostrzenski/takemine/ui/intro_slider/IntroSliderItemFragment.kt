package com.pkostrzenski.takemine.ui.intro_slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pkostrzenski.takemine.R
import kotlinx.android.synthetic.main.fragment_intro_slider_item.*

class IntroSliderItemFragment( private val sliderItem: SliderItem) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro_slider_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView.text = sliderItem.title
        descriptionTextView.text = sliderItem.description
        imageView.setImageResource(sliderItem.imageId)
        itemLayout.setBackgroundResource(sliderItem.backgroundId)
    }

}