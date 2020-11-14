package com.pkostrzenski.takemine.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseViewModel

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val mutableNavigateToMain = MutableLiveData<Boolean>()
    private val mutableNavigateToBalance = MutableLiveData<Boolean>()
    private val mutableNavigateToAbout = MutableLiveData<Boolean>()

    val navigateToMain: LiveData<Boolean> = mutableNavigateToMain
    val navigateToBalance: LiveData<Boolean> = mutableNavigateToBalance
    val navigateToAbout: LiveData<Boolean> = mutableNavigateToAbout


    fun drawerItemClicked(itemId: Int){
        when(itemId){
            R.id.nav_home -> mutableNavigateToMain.value = true
            R.id.nav_history -> mutableNavigateToBalance.value = true
            R.id.nav_about -> mutableNavigateToAbout.value = true
        }
    }
}