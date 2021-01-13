package com.pkostrzenski.takemine.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.api.ApiFactory
import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : BaseActivity() {

    private lateinit var model: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        supportActionBar?.title = "Zobacz przedmiot"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initModel()

        model.productIdReceived(intent.getStringExtra("product_id").toInt())
        buyProductButton.setOnClickListener { model.buyProductClicked() }
    }

    private fun initModel() {
        model = ViewModelProvider(this).get(ProductViewModel::class.java)
        model.uiEnabled.observe(this, Observer { enableViews(it ?: true) })
        model.message.observe(this, Observer { it?.run{ showToast(it) }})
        model.product.observe(this, Observer { it?.run { displayProduct(it) } })
        model.ownerEmail.observe(this, Observer { it?.run{ displayOwnerEmail(it) } })
    }

    private fun enableViews(enabled: Boolean) {
        buyProductButton.isEnabled = enabled
    }

    private fun displayProduct(product: Product) {
        productTitle.text = product.name
        productDescription.text = product.description

        val baseUrl: String = ApiFactory.URL
        Glide.with(this)
            .load("${baseUrl}/api/pictures/${product.pictures.first().path}")
            .into(productMainImage)

        var locationsString = ""
        product.locations.forEach {
            locationsString += "- ${it.name}\n"
            locationsString += "  W godzinach: ${it.fromHour}-${it.toHour}\n"
            locationsString += "  W dni: \n"
            if (it.monday) locationsString += "  - Poniedziałek\n"
            if (it.tuesday) locationsString += "  - Wtorek\n"
            if (it.wednesday) locationsString += "  - Środa\n"
            if (it.thursday) locationsString += "  - Czwartek\n"
            if (it.friday) locationsString += "  - Piątek\n"
            if (it.saturday) locationsString += "  - Sobota\n"
            if (it.sunday) locationsString += "  - Niedziela\n"
        }

        productAddress.text = locationsString

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        productRecyclerView.layoutManager = linearLayoutManager
        productRecyclerView.adapter = PhotoRecyclerAdapter(product.pictures.toList(), this)
    }

    private fun displayOwnerEmail(email: String) {
        buyProductButton.visibility = View.GONE
        ownerEmailText.text = "E-mail do właściciela:\n${email}"
        ownerEmailText.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }
}
