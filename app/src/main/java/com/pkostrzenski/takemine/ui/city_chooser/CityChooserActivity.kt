package com.pkostrzenski.takemine.ui.city_chooser

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.City
import com.pkostrzenski.takemine.ui.base.BaseActivity
import com.pkostrzenski.takemine.ui.intro_slider.IntroSliderActivity
import com.pkostrzenski.takemine.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_city_chooser.*

class CityChooserActivity : BaseActivity() {

    private lateinit var model: CityChooserViewModel
    private lateinit var citiesAdapter: CitiesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_chooser)

        initModel()
        setupActionBar()
        setupClickListeners()
        setupTextChangeListener()
        setupRecyclerView()
    }

    private fun initModel(){
        model = ViewModelProvider(this).get(CityChooserViewModel::class.java)
        model.message.observe(this, Observer { showToast(it) })
        model.loadingVisible.observe(this, Observer { setLoadingVisibility(it ?: false) })
        model.navigateToMain.observe(this, Observer { if(it == true) navigateToMainAndRestartActivities() })
        model.cities.observe(this, Observer { it?.let { displayCities(it) } })
    }

    private fun setupActionBar(){
        supportActionBar?.title = getString(R.string.choose_city)
        if(intent.getBooleanExtra(DISPLAY_BACK_BUTTON_INTENT, false))
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupClickListeners(){

    }

    private fun setupTextChangeListener(){
        citiesSearchInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                model.filterCitiesBySubname(text.toString())
            }
        })
    }

    private fun setupRecyclerView(){
        citiesAdapter = CitiesRecyclerAdapter(emptyList()){ city -> model.chooseCity(city) }
        citiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CityChooserActivity)
            adapter = citiesAdapter
        }
    }

    private fun displayCities(cities: List<City>){
        citiesAdapter.cities = cities
        citiesAdapter.notifyDataSetChanged()
        citiesSearchInputLayout.visibility = View.VISIBLE
    }

    private fun setLoadingVisibility(visible: Boolean){
        citiesProgressBar.visibility = if(visible) View.VISIBLE else View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    private fun navigateToMainAndRestartActivities(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    companion object {
        const val DISPLAY_BACK_BUTTON_INTENT = "display_back_button_intent"
    }
}
