package com.pkostrzenski.takemine.ui.main.fragments.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkostrzenski.takemine.R

import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.ui.base.BaseFragment
import com.pkostrzenski.takemine.ui.post_product.PostProductActivity
import com.pkostrzenski.takemine.ui.product.ProductActivity
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {
    private lateinit var model: MainFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postProductButton.setOnClickListener {
            model.postProductButtonClicked()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupModel(){
        model = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        model.message.observe(this, Observer { displayToast(it) })
        model.uiEnabled.observe(this, Observer { setViewsVisible(it) })
        model.products.observe(this, Observer { it?.run { setupRecyclerView(it) } })
        model.navigateToProduct.observe(this, Observer { it?.run { navigateToProduct(it)} })
        model.navigateToPostProduct.observe(this, Observer { it?.run { navigateToPostProduct(it) } })
    }

    private fun setupRecyclerView(products: List<Product>){
        if (products.isNotEmpty()) {
            val linearLayoutManager = LinearLayoutManager(activity)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = ProductRecyclerAdapter(products, context!!) {  productId ->
                model.productClicked(productId)
            }
        }
        else noGroupsText.visibility = View.VISIBLE
    }

    private fun setViewsVisible(visible: Boolean){
        val visibility = if(visible) View.VISIBLE else View.INVISIBLE
        recyclerView.visibility = visibility
        progressBar.visibility = if(visible) View.INVISIBLE else View.VISIBLE
    }

    private fun navigateToProduct(productId: Int){
        val intent = Intent(context, ProductActivity::class.java)
        intent.putExtra("product_id", productId.toString())
        activity?.startActivity(intent)
    }

    private fun navigateToPostProduct(create: Boolean){
        val intent = Intent(context, PostProductActivity::class.java)
        activity?.startActivity(intent)
    }
}
