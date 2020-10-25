package com.pkostrzenski.takemine.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun displayToast(message: String?){
        if(message != null && message.isNotBlank())
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun displayToast(message: Int?){
        message?.let { Toast.makeText(activity, getString(it), Toast.LENGTH_LONG).show() }
    }

}