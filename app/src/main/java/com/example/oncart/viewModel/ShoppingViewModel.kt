package com.example.oncart.viewModel

import android.app.Activity
import androidx.lifecycle.*
import com.example.oncart.R
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import com.example.oncart.networkServices.ProductAPI
import com.example.oncart.room.AppDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch

class ShoppingViewModel(private val activity: Activity, private val dao: AppDao): ViewModel() {

    private var database = FirebaseDatabase.getInstance()
    private var auth = FirebaseAuth.getInstance()

    var _list = MutableLiveData<ArrayList<ProductItems>>()
    lateinit var _cartList : LiveData<MutableList<ProductItems>>
    lateinit var _likedList : LiveData<List<LikedItems>>
    lateinit var _cart : LiveData<MutableList<ProductItems>>
    var _notificationProducts = MutableLiveData<ArrayList<ProductItems>>()
    var _notification = MutableLiveData<ArrayList<ProductItems>>()
    var _listMobile = MutableLiveData<ArrayList<ProductItems>>()
    var _listLaptop = MutableLiveData<ArrayList<ProductItems>>()
    var _listTelevision = MutableLiveData<ArrayList<ProductItems>>()
    var _listTop = MutableLiveData<ArrayList<ProductItems>>()
    var _listBottom = MutableLiveData<ArrayList<ProductItems>>()

    init {
        getMobile()
        getLaptop()
        getTele()
        getTop()
        getBottom()
    }

    fun getCartItems() {
        viewModelScope.launch {
            _cartList = dao.getCartItems().asLiveData()
        }
    }

    fun getCart() {
        viewModelScope.launch {
            _cart = dao.getCartItems().asLiveData()
        }
    }

    fun addToCart(productItem: ProductItems) {
        viewModelScope.launch {
            dao.addToCart(productItem)
        }
    }

    fun removeFromCart(productItem: ProductItems) {
        viewModelScope.launch {
            dao.removeFromCart(productItem)
        }
    }

    fun addToLiked(likedItem: LikedItems) {
        viewModelScope.launch {
            dao.addToLiked(likedItem)
        }
    }

    fun removeFromLiked(likedItem: LikedItems) {
        viewModelScope.launch {
            dao.removeFromLiked(likedItem)
        }
    }

    fun getLikedItems() {
        viewModelScope.launch {
            _likedList = dao.getLikedItems().asLiveData()
        }
    }
    fun getMobileProducts(product: String) {
        viewModelScope.launch {
            try {
                _list.value = ProductAPI.retrofitService.getMobiles(product).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {
                activity.neHome?.makeVisible()
            }
        }
    }
    private fun updateCart(productItem: ProductItems) {
        viewModelScope.launch {
            try {
                dao.updateCart(productItem)
            }
            catch (e: Exception) {}
        }
    }
    fun increaseQuantity(productItem: ProductItems) {
        productItem.apply {
            quantity = quantity?.plus(1)
            updateCart(this)
        }
    }
    fun decreaseQuantity(productItem: ProductItems) {
        productItem.apply {
            if(quantity==1) {}
            else {
                quantity = quantity?.minus(1)
                updateCart(this)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            dao.clearCart()
        }
    }
    fun getNotificationProducts() {
        viewModelScope.launch {
            try {
                _notificationProducts.value = ProductAPI.retrofitService.getNotifications().dataArray as ArrayList<ProductItems>
            }catch (e:Exception){}
        }
    }

    fun getNotification() {
        viewModelScope.launch {
            try {
                _notification.value = ProductAPI.retrofitService.getNotifications().dataArray as ArrayList<ProductItems>
            }catch (e:Exception){}
        }
    }
    private fun getMobile() {
        viewModelScope.launch {
            try {
                _listMobile.value = ProductAPI.retrofitService.getMobiles(activity.getString(R.string.mobile)).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {}
        }
    }
    private fun getLaptop() {
        viewModelScope.launch {
            try {
                _listLaptop.value = ProductAPI.retrofitService.getMobiles(activity.getString(R.string.laptops)).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {}
        }
    }
    private fun getTele() {
        viewModelScope.launch {
            try {
                _listTelevision.value = ProductAPI.retrofitService.getMobiles(activity.getString(R.string.Television)).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {}
        }
    }
    private fun getTop() {
        viewModelScope.launch {
            try {
                _listTop.value = ProductAPI.retrofitService.getMobiles(activity.getString(R.string.men_top_wear)).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {}
        }
    }
    private fun getBottom() {
        viewModelScope.launch {
            try {
                _listBottom.value = ProductAPI.retrofitService.getMobiles(activity.getString(R.string.men_bottom_wear)).dataArray as ArrayList<ProductItems>
            }
            catch (e: Exception) {}
        }
    }
}

class ViewModelFactory(private val activity: Activity, private val dao: AppDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            return ShoppingViewModel(activity, dao) as T
        }
        throw IllegalStateException("Unknown")
    }
}