package com.example.oncart.viewModel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.oncart.Utils.Utils
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.ProductItems
import com.example.oncart.networkServices.ProductAPI
import com.example.oncart.networkServices.Products
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalStateException

class ShoppingViewModel(private val activity: Activity): ViewModel() {

    var _list = MutableLiveData<ArrayList<ProductItems>>()

    fun getMobileProducts(product: String) {
        viewModelScope.launch {
            try {
                _list.value = ProductAPI.retrofitService.getMobiles(product).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {
                activity.neHome.makeVisible()
            }
        }
    }
}

class ViewModelFactory(private val activity: Activity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            return ShoppingViewModel(activity) as T
        }
        throw IllegalStateException("Unknown")
    }

}