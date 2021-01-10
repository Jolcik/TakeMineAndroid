package com.pkostrzenski.takemine.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseActivity
import com.pkostrzenski.takemine.ui.city_chooser.CityChooserActivity
import com.pkostrzenski.takemine.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var model: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initModel()
        loginButton.setOnClickListener { login() }
        registerButton.setOnClickListener { model.registerClicked() }
    }

    private fun initModel(){
        model = ViewModelProvider(this).get(LoginViewModel::class.java)
        model.message.observe(this, Observer { it?.run { showToast(getString(it)) } })
        model.uiEnabled.observe(this, Observer { enableViews(it ?: true) })
        model.navigateToLogged.observe(this, Observer { if(it == true) navigateToCityChooserActivity() })
        model.navigateToRegister.observe(this, Observer { if(it == true) navigateToRegisterActivity() })
    }

    private fun login(){
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        model.authenticateUser(email, password)
    }

    private fun enableViews(enabled: Boolean){
        val views = listOf(emailInputLayout, passwordInputLayout,
            loginButton, registerButton)
        views.forEach { it.isEnabled = enabled }

        progressBar.visibility = if(enabled) View.GONE else View.VISIBLE
    }

    private fun navigateToCityChooserActivity(){
        val intent = Intent(this, CityChooserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
