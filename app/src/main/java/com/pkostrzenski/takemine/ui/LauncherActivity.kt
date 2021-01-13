package com.pkostrzenski.takemine.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pkostrzenski.takemine.api.ApiFactory
import com.pkostrzenski.takemine.repository.MainRepository
import com.pkostrzenski.takemine.ui.intro_slider.IntroSliderActivity
import com.pkostrzenski.takemine.ui.main.MainActivity
import com.pkostrzenski.takemine.utils.CacheSaver
import com.pkostrzenski.takemine.utils.PushMessagingService
import com.pkostrzenski.takemine.utils.SharedPreferencesSaver

class LauncherActivity : AppCompatActivity() {

    val cacheSaver: CacheSaver = SharedPreferencesSaver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cacheSaver.retrieveToken()?.let {
            ApiFactory.token = it
            MainRepository.userId = cacheSaver.retrieveUserId()?.toInt()
            navigateToActivity(MainActivity::class.java)
        } ?: navigateToActivity(IntroSliderActivity::class.java)
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val activityIntent = Intent(this, activityClass)

        val pushProductId = intent.extras?.getString(PushMessagingService.PRODUCT_ID_KEY)
        if(pushProductId != null)
            activityIntent.putExtra(MainActivity.PRODUCT_ID_INTENT, pushProductId)

        startActivity(activityIntent)
        finish()
    }
}
