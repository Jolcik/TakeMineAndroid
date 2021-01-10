package com.pkostrzenski.takemine.ui.post_product

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Picture
import com.pkostrzenski.takemine.ui.photo_picker.PhotoPickerBaseActivity
import kotlinx.android.synthetic.main.activity_post_product.*
import java.util.*

class PostProductActivity : PhotoPickerBaseActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener
{

    private lateinit var model: PostProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_product)

        supportActionBar?.title = "Dodaj przedmiot"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initModel()
        setupPickers()
        addPhotoIcon.setOnClickListener { model.uploadPhotoClicked() }
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(PostProductViewModel::class.java)
        model.navigateToPhotoPicker.observe(this, Observer { if(it == true) pickPhoto() })
        model.navigateToMain.observe(this, Observer { finish() })
        model.uiEnabled.observe(this, Observer { enableViews(it ?: true) })
        model.pictures.observe(this, Observer { updatePictures(it ?: listOf()) })
    }

    private fun setupPickers(){
        product_date_input.setOnClickListener { openDatePicker() }
        product_time_input.setOnClickListener { openTimePicker() }
    }

    // we chose the date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // show it inside the input
        val dayString = String.format("%02d", dayOfMonth)
        val monthString = String.format("%02d", month + 1)
        product_date_input.setText("$dayString.$monthString.$year")

        // if the next picker is empty, focus on it -> provides consequent flow
        if(product_time_input.text.isNullOrBlank())
            openTimePicker()
    }

    // we chose the time
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        // show it inside the input
        val hourString = String.format("%02d", hour)
        val minuteString = String.format("%02d", minute)
        product_time_input.setText("$hourString:$minuteString")
    }

    // utils functions - showing the pickers, closing the keyboard and going back to previous activity
    private fun openDatePicker(){
        val mYear = Calendar.getInstance().get(Calendar.YEAR)
        val mMonth = Calendar.getInstance().get(Calendar.MONTH)
        val mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, this, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun openTimePicker(){
        val mHour = Calendar.getInstance().get(Calendar.HOUR)
        val mMinute = Calendar.getInstance().get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, mHour, mMinute, true)
        timePickerDialog.show()
    }

    private fun enableViews(enabled: Boolean) {
        postProductScrollView.isEnabled = enabled
        postProductProgressBar.visibility = if(enabled) View.GONE else View.VISIBLE
    }

    private fun updatePictures(pictures: List<Picture>) {
        addPhotoCounter.text = "Dodano zdjęć: ${pictures.size}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            convertResultDataToByteArray(data)?.let { photo ->
                model.uploadPhoto(photo)
            } ?: showToast("Wystąpił lokalny błąd, spróbuj ponownie!")
        }
    }

}
