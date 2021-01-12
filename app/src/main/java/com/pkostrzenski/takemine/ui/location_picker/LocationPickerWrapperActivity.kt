package com.pkostrzenski.takemine.ui.location_picker

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TimePicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseActivity
import com.schibstedspain.leku.*
import com.schibstedspain.leku.LocationPickerActivity
import kotlinx.android.synthetic.main.activity_location_picker.*
import java.util.*

class LocationPickerWrapperActivity : BaseActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var model: LocationPickerWrapperViewModel
    private var whichTimePickerClicked: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

        supportActionBar?.title = "Wybierz lokalizację"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupPickers()
        initModel()
        addCoordinatesButton.setOnClickListener { model.pickCoordinatesClicked() }
        addLocationButton.setOnClickListener { submitLocationClicked() }
    }

    private fun setupPickers() {
        locationFromHourInput.setOnClickListener { openTimePicker(1) }
        locationToHourInput.setOnClickListener { openTimePicker(2) }
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(LocationPickerWrapperViewModel::class.java)
        model.navigateToMain.observe(this, Observer {
            if (it == true) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
        model.navigateToLocationPicker.observe(this, Observer { if (it == true) openLocationPicker() })
        model.message.observe(this, Observer { it?.run { showToast(getString(it)) } })
        model.address.observe(this, Observer { locationAddressInput.setText(it) })
        model.fromHour.observe(this, Observer { locationFromHourInput.setText(it) })
        model.toHour.observe(this, Observer { locationToHourInput.setText(it) })
    }

    private fun openLocationPicker() {
        model.city?.let {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(it.centerLat, it.centerLng)
                .withSearchZone("pl_PL")
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(applicationContext)

            startActivityForResult(locationPickerIntent, 1)
        } ?: showToast("Nie znaleziono wybranego miasta!")
    }

    private fun submitLocationClicked() {
        model.submitLocationClicked(
            monday = mondayCheckBox.isChecked,
            tuesday = tuesdayCheckBox.isChecked,
            wednesday = wednesdayCheckBox.isChecked,
            thursday = thursdayCheckBox.isChecked,
            friday = fridayCheckBox.isChecked,
            saturday = saturdayCheckBox.isChecked,
            sunday = sundayCheckBox.isChecked
        )
    }

    private fun openTimePicker(whichTimePicker: Int) {
        val mHour = Calendar.getInstance().get(Calendar.HOUR)
        val mMinute = Calendar.getInstance().get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, mHour, mMinute, true)
        whichTimePickerClicked = whichTimePicker
        timePickerDialog.show()
    }

    // we chose the time
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        // show it inside the input
        val hourString = String.format("%02d", hour)
        val minuteString = String.format("%02d", minute)
        if (whichTimePickerClicked == 1)
            model.fromHourSet("$hourString:$minuteString")
        else
            model.toHourSet("$hourString:$minuteString")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK")
            if (requestCode == 1) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())

                model.coordinatesReceived(latitude, longitude, address)
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }

}
