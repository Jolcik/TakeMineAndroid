package com.pkostrzenski.takemine.ui.post_product

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Category
import com.pkostrzenski.takemine.models.ItemType
import com.pkostrzenski.takemine.models.Location
import com.pkostrzenski.takemine.models.Picture
import com.pkostrzenski.takemine.ui.location_picker.LocationPickerWrapperActivity
import com.pkostrzenski.takemine.ui.photo_picker.PhotoPickerBaseActivity
import kotlinx.android.synthetic.main.activity_post_product.*


class PostProductActivity : PhotoPickerBaseActivity() {

    private lateinit var model: PostProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_product)

        supportActionBar?.title = "Dodaj przedmiot"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initModel()
        addPhotoIcon.setOnClickListener { model.uploadPhotoClicked() }
        addLocationIcon.setOnClickListener { model.pickLocationClicked() }
        postProductApplyButton.setOnClickListener {
            model.postProductClicked(productNameInput.text.toString(), productDescriptionInput.text.toString())
        }
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(PostProductViewModel::class.java)
        model.navigateToPhotoPicker.observe(this, Observer { if(it == true) pickPhoto() })
        model.navigateToLocationPicker.observe(this, Observer { if(it == true) navigateToLocationPicker() })
        model.navigateToMain.observe(this, Observer { finish() })
        model.uiEnabled.observe(this, Observer { enableViews(it ?: true) })
        model.categories.observe(this, Observer { if (it != null) fillCategories(it) })
        model.itemTypes.observe(this, Observer { if (it != null) fillItemTypes(it) })
        model.pictures.observe(this, Observer { updatePictures(it ?: setOf()) })
        model.locations.observe(this, Observer { if (it != null) updateLocations(it) })
    }

    private fun enableViews(enabled: Boolean) {
        postProductScrollView.isEnabled = enabled
        postProductProgressBar.visibility = if(enabled) View.GONE else View.VISIBLE
    }

    private fun fillCategories(categories: List<Category>) {
        val adapter = ArrayAdapter<String>(
            this, android.R.layout.select_dialog_item, categories.map { it.name }
        )
        categorySpinner.adapter = adapter
        categorySpinner.onItemSelectedListener = object : OnItemSelectedListener {
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
        itemTypeSpinner.adapter = adapter
        itemTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                model.itemTypePicked(itemTypes[position])
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun updatePictures(pictures: Set<Picture>) {
        addPhotoCounter.text = "Dodano zdjęć: ${pictures.size}"
    }

    private fun updateLocations(locations: Set<Location>) {
        if (locations.isNotEmpty()) {
            addLocationIcon.visibility = View.GONE
            addAnotherLocationIcon.visibility = View.VISIBLE
            addAnotherLocationText.visibility = View.VISIBLE
            var locationsString = ""
            locations.forEach { locationsString += "- ${it.name.slice(0..32)}...\n" }

            addressesList.text = locationsString
        }
    }

    private fun navigateToLocationPicker() {
        val intent = Intent(this, LocationPickerWrapperActivity::class.java)
        startActivityForResult(intent, LOCATION_REQUEST_CODE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            model.gotActivityOkResult()

        if(requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            convertResultDataToByteArray(data)?.let { photo ->
                model.uploadPhoto(photo)
            } ?: showToast("Wystąpił lokalny błąd, spróbuj ponownie!")
        }
    }

    companion object {
        val LOCATION_REQUEST_CODE = 1234
    }

}
