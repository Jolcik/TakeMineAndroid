package com.pkostrzenski.takemine.ui.intro_slider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.login.LoginActivity
import com.pkostrzenski.takemine.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_intro_slider.*

class IntroSliderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)

        val adapter = IntroSliderPagerAdapter(
            supportFragmentManager,
            listOf(
                SliderItem(getString(R.string.obtain_title), getString(R.string.obtain_description), R.drawable.obtain, R.drawable.slider_background_1),
                SliderItem(getString(R.string.give_away_title), getString(R.string.give_away_description), R.drawable.give, R.drawable.slider_background_1),
                SliderItem(getString(R.string.take_action_title), getString(R.string.take_action_description), R.drawable.recycle, R.drawable.slider_background_1)
            )
        )

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        window.statusBarColor = resources.getColor(android.R.color.holo_blue_bright)
        sliderNextButton.setOnClickListener {
            if(viewPager.currentItem < adapter.count - 1)
                viewPager.currentItem += 1
            else navigateFurther()
        }
        sliderSkipButton.setOnClickListener { navigateFurther() }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(a: Int, b: Float, c: Int) {}
            override fun onPageSelected(position: Int) {
                if(position == adapter.count - 1) {
                    sliderNextButton.text = getString(R.string.get_started)
                    sliderSkipButton.visibility = View.GONE
                } else {
                    sliderNextButton.text = getString(R.string.next)
                    sliderSkipButton.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun navigateFurther(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    class IntroSliderPagerAdapter(
        fm: FragmentManager,
        private val items: List<SliderItem>
    ) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int) = IntroSliderItemFragment(items[position])
        override fun getCount() = items.size
    }
}
