package com.pkostrzenski.takemine.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseActivity
import com.pkostrzenski.takemine.ui.city_chooser.CityChooserActivity
import com.pkostrzenski.takemine.ui.intro_slider.IntroSliderActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    private lateinit var model: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initModel()
        registerButton.setOnClickListener { registerButtonClicked() }
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun initModel(){
        model = ViewModelProvider(this).get(RegisterViewModel::class.java)
        model.message.observe(this, Observer { showToast(it)})
        model.uiEnabled.observe(this, Observer { setViewsEnabled(it ?: true) })
        model.navigateToIntroSlider.observe(this, Observer { if(it == true) navigateToIntroSliderAndFinish()})
    }

    private fun registerButtonClicked(){
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()
        model.registerClicked(email, password, confirmPassword)
    }

    private fun setViewsEnabled(enabled: Boolean){
        val views = listOf(emailInput, passwordInput, confirmPasswordInput)
        views.forEach { it.isEnabled = enabled }
        registerProgressBar.visibility = if(enabled) View.GONE else View.VISIBLE
    }

    private fun navigateToIntroSliderAndFinish(){
        val intent = Intent(this, CityChooserActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    // so when we click back arrow we go back to login activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }
}
