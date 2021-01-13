package com.pkostrzenski.takemine.ui.main.fragments.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainFragmentViewModel(application: Application): BaseViewModel(application) {

    private val mutableProducts = MutableLiveData<List<Product>>()
    private val mutableNavigateToPostProduct = MutableLiveData<Boolean>()
    private val mutableNavigateToProduct = MutableLiveData<Int>()
    private val mutableNavigateToAddNotifier = MutableLiveData<Boolean>()

    val products: LiveData<List<Product>> = mutableProducts
    val navigateToPostProduct: LiveData<Boolean> = mutableNavigateToPostProduct
    val navigateToProduct: LiveData<Int> = mutableNavigateToProduct
    val navigateToAddNotifier: LiveData<Boolean> = mutableNavigateToAddNotifier

    init {
        fetchProducts()
    }

    fun fetchProducts() = viewModelScope.launch {
        disableUi()

        when(val fetchedProducts = repository.getProductsFromCity()) {
            null -> displayMessage(R.string.connection_error)
            else -> {
                mutableProducts.value = fetchedProducts
                enableUi()
            }
        }
    }

    fun postProductButtonClicked() {
        mutableNavigateToPostProduct.value = true
    }

    fun productClicked(productId: Int) {
        mutableNavigateToProduct.value = productId
    }

    fun addNotifierButtonClicked() {
        mutableNavigateToAddNotifier.value = true
    }
}