package com.pkostrzenski.takemine.ui.add_notifier

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Category
import com.pkostrzenski.takemine.models.ItemType
import com.pkostrzenski.takemine.models.Location
import com.pkostrzenski.takemine.ui.location_picker.LocationPickerWrapperActivity
import kotlinx.android.synthetic.main.activity_add_notifier.*

class AddNotifierActivity : AppCompatActivity() {

    private lateinit var model: AddNotifierViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notifier)

        supportActionBar?.title = "Dodaj powiadomienie"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initModel()
        addNotifierLocationIcon.setOnClickListener { model.pickLocationClicked() }
        addNotifierApplyButton.setOnClickListener { model.addNotifierClicked() }
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(AddNotifierViewModel::class.java)
        model.navigateToLocationPicker.observe(this, Observer { if(it == true) navigateToLocationPicker() })
        model.navigateToMain.observe(this, Observer { finish() })
        model.uiEnabled.observe(this, Observer { enableViews(it ?: true) })
        model.categories.observe(this, Observer { if (it != null) fillCategories(it) })
        model.itemTypes.observe(this, Observer { if (it != null) fillItemTypes(it) })
        model.locations.observe(this, Observer { if (it != null) updateLocations(it) })
    }

    private fun enableViews(enabled: Boolean) {
        addNotifierScrollView.isEnabled = enabled
        addNotifierProgressBar.visibility = if(enabled) View.GONE else View.VISIBLE
    }

    private fun fillCategories(categories: List<Category>) {
        val adapter = ArrayAdapter<String>(
            this, android.R.layout.select_dialog_item, categories.map { it.name }
        )
        notifierCategorySpinner.adapter = adapter
        notifierCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                model.categoryPicked(categories[position])
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        model.categoryPicked(categories.first())
    }

    private fun fillItemTypes(itemTypes: List<ItemType>) {
        val adapter = ArrayAdapter<String>(
            this, android.R.layout.select_dialog_item, itemTypes.map { it.name }
        )
        notifierItemTypeSpinner.adapter = adapter
        notifierItemTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                model.itemTypePicked(itemTypes[position])
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun updateLocations(locations: Set<Location>) {
        if (locations.isNotEmpty()) {
            addNotifierLocationIcon.visibility = View.GONE
            addAnotherNotifierLocationIcon.visibility = View.VISIBLE
            addAnotherNotifierLocationText.visibility = View.VISIBLE
            var locationsString = ""
            locations.forEach { locationsString += "- ${it.name.slice(0..32)}...\n" }

            notifierAddressesList.text = locationsString
        }
    }

    private fun navigateToLocationPicker() {
        val intent = Intent(this, LocationPickerWrapperActivity::class.java)
        startActivityForResult(intent, LOCATION_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            model.gotActivityOkResult()
    }

    companion object {
        val LOCATION_REQUEST_CODE = 4567
    }
}
