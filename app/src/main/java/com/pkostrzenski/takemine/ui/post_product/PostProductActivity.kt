package com.pkostrzenski.takemine.ui.post_product

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_post_product.*
import java.util.*

class PostProductActivity : BaseActivity(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_product)

        supportActionBar?.title = "Post product"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupPickers()
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

}
