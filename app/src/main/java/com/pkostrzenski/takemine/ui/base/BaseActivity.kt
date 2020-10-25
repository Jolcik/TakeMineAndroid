package com.pkostrzenski.takemine.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

open class BaseActivity : AppCompatActivity() {

    fun showToast(message: String){
        if(message.isNotBlank()) Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(messageId: Int?){
        messageId?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
    }
}
