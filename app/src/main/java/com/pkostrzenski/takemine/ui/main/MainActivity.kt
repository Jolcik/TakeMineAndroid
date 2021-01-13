package com.pkostrzenski.takemine.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseActivity
import com.pkostrzenski.takemine.ui.main.fragments.about.AboutFragment
import com.pkostrzenski.takemine.ui.main.fragments.history.HistoryFragment
import com.pkostrzenski.takemine.ui.main.fragments.main.MainFragment
import com.pkostrzenski.takemine.ui.product.ProductActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mainFragment = MainFragment()
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setNavigationItemSelectedListener(this)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_content, mainFragment)
            .commit()

        initModel()
        setupToolbar()

        intent.getStringExtra(PRODUCT_ID_INTENT)?.let { id -> navigateToProduct(id.toInt()) }
    }

    private fun initModel(){
        model = ViewModelProvider(this).get(MainViewModel::class.java)
        model.navigateToMain.observe(this, Observer { navigateBackToMain() })
        model.navigateToBalance.observe(this, Observer { navigateToFragment(HistoryFragment()) })
        model.navigateToAbout.observe(this, Observer { navigateToFragment(AboutFragment()) })
    }

    private fun setupToolbar() {
        setSupportActionBar(mainToolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
            setDisplayHomeAsUpEnabled(true)
            title = "Wystawione przedmioty"
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        if(navigationView.checkedItem?.itemId == menuItem.itemId)
            return true // do not do anything if we clicked already checked item

        model.drawerItemClicked(menuItem.itemId)
        menuItem.isChecked = true
        supportActionBar?.title = menuItem.title
        return true
    }

    private fun navigateBackToMain(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_content, mainFragment)
            .commit()
    }

    private fun navigateToFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        val currentFragments = supportFragmentManager.fragments

        if(currentFragments.size == 2)
            transaction.remove(currentFragments.last())

        transaction
            .add(R.id.fragment_content, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 1)
            onNavigationItemSelected(navigationView.menu.findItem(R.id.nav_home))
        else
            super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(Gravity.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigateToProduct(productId: Int){
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("product_id", productId.toString())
        startActivity(intent)
    }

    companion object {
        val PRODUCT_ID_INTENT = "product_id"
    }
}
