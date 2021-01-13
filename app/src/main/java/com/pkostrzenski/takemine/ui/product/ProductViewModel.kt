package com.pkostrzenski.takemine.ui.product

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): BaseViewModel(application) {

    val mutableProduct: MutableLiveData<Product> = MutableLiveData()
    val mutableOwnerEmail: MutableLiveData<String> = MutableLiveData()

    val product: LiveData<Product> = mutableProduct
    val ownerEmail: LiveData<String> = mutableOwnerEmail


    fun productIdReceived(productId: Int) {
        viewModelScope.launch {
            disableUi()
            when(val response = repository.getProduct(productId)) {
                null -> displayMessage(R.string.connection_error)
                else -> mutableProduct.value = response
            }
            enableUi()
        }
    }

    fun buyProductClicked() {
        viewModelScope.launch {
            disableUi()
            when(val response = repository.buyProduct(product.value!!.id!!)) {
                null -> displayMessage(R.string.already_bought)
                else -> mutableOwnerEmail.value = response.email
            }
            enableUi()
        }
    }

}