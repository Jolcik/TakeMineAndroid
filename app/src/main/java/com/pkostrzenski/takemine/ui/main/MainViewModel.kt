package com.pkostrzenski.takemine.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import com.pkostrzenski.takemine.utils.SharedPreferencesSaver
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val mutableNavigateToMain = MutableLiveData<Boolean>()
    private val mutableNavigateToBalance = MutableLiveData<Boolean>()
    private val mutableNavigateToAbout = MutableLiveData<Boolean>()

    val navigateToMain: LiveData<Boolean> = mutableNavigateToMain
    val navigateToBalance: LiveData<Boolean> = mutableNavigateToBalance
    val navigateToAbout: LiveData<Boolean> = mutableNavigateToAbout

    init {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if(!task.isSuccessful){
                    Log.w(TAG, "fail", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token!!
                Log.d(TAG, token)
                viewModelScope.launch {
                    repository.addFirebaseToken(token)
                }
            })
    }

    fun drawerItemClicked(itemId: Int){
        when(itemId){
            R.id.nav_home -> mutableNavigateToMain.value = true
            R.id.nav_history -> mutableNavigateToBalance.value = true
            R.id.nav_about -> {mutableNavigateToAbout.value = true; SharedPreferencesSaver.clear()}
        }
    }

    companion object {
        val TAG = "LoginViewModel"
    }
}