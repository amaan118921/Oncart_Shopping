package com.example.oncart.viewModel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.oncart.R
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.AddressModel
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import com.example.oncart.networkServices.ProductAPI
import com.example.oncart.room.AppDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch

class ShoppingViewModel(private val dao: AppDao, application: Application): AndroidViewModel(application) {

    private var database = FirebaseDatabase.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private var array = arrayListOf<ProductItems>()
    private var _list = MutableLiveData<ArrayList<ProductItems>>()
    var list: LiveData<ArrayList<ProductItems>> = _list
    lateinit var _cartList : LiveData<MutableList<ProductItems>>
    lateinit var _likedList : LiveData<List<LikedItems>>
    lateinit var _cart : LiveData<MutableList<ProductItems>>
    var _notificationProducts = MutableLiveData<ArrayList<ProductItems>>()
    var _notification = MutableLiveData<ArrayList<ProductItems>>()
    private var _listMobile = MutableLiveData<ArrayList<ProductItems>>()
    var listMobile: LiveData<ArrayList<ProductItems>> = _listMobile
    private var _listLaptop = MutableLiveData<ArrayList<ProductItems>>()
    var listLaptop: LiveData<ArrayList<ProductItems>> = _listLaptop
    private var _listTelevision = MutableLiveData<ArrayList<ProductItems>>()
    var listTelevision: LiveData<ArrayList<ProductItems>> = _listTelevision
    private var _listTop = MutableLiveData<ArrayList<ProductItems>>()
    var listTop: LiveData<ArrayList<ProductItems>> = _listTop
    private var _listBottom = MutableLiveData<ArrayList<ProductItems>>()
    var listBottom: LiveData<ArrayList<ProductItems>> = _listBottom
    lateinit var _address : LiveData<List<AddressModel>>

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
//        viewModelScope.launch {
//            try {
//                _list.value = ProductAPI.retrofitService.getMobiles(product).dataArray as ArrayList<ProductItems>
//            }
//            catch (e: Exception) {
//                activity.neHome?.makeVisible()
//            }
//        }
        _list.value = arrayListOf()
        array.clear()
        val databaseRef = database.reference.child("products").child(product)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap: DataSnapshot in snapshot.children) {
                    val data = snap.getValue(ProductItems::class.java)
                    array.add(data!!)
                }
                _list.value = array
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
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
            _listMobile.value = arrayListOf()
            val list = arrayListOf<ProductItems>()
            val databaseRef = database.reference.child("products").child(getContext().getString(R.string.mobile))
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        val data = snap.getValue(ProductItems::class.java)
                        list.add(data!!)
                    }
                    _listMobile.value = list
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    private fun getLaptop() {
        viewModelScope.launch {
            _listLaptop.value = arrayListOf()
            val list = arrayListOf<ProductItems>()
            val databaseRef = database.reference.child("products").child(getContext().getString(R.string.laptops))
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        val data = snap.getValue(ProductItems::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    _listLaptop.value = list
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    private fun getTele() {
        viewModelScope.launch {
            _listTelevision.value = arrayListOf()
            val list = arrayListOf<ProductItems>()
            val databaseRef = database.reference.child("products").child(getContext().getString(R.string.Television))
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        val data = snap.getValue(ProductItems::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    _listTelevision.value = list
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    private fun getTop() {
        viewModelScope.launch {
            _listTop.value = arrayListOf()
            val list = arrayListOf<ProductItems>()
            val databaseRef = database.reference.child("products").child(getContext().getString(R.string.men_top_wear))
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        val data = snap.getValue(ProductItems::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    _listTop.value = list
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    private fun getBottom() {
        viewModelScope.launch {
            _listBottom.value = arrayListOf()
            val list = arrayListOf<ProductItems>()
            val databaseRef = database.reference.child("products").child(getContext().getString(R.string.men_bottom_wear))
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snap: DataSnapshot in snapshot.children) {
                        val data = snap.getValue(ProductItems::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                    }
                    _listBottom.value = list
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    fun addAddress(addressModel: AddressModel) {
        viewModelScope.launch {
            try {
                dao.addAddress(addressModel)
            }catch (e:Exception){}
        }
    }
    fun updateAddress(addressModel: AddressModel) {
        viewModelScope.launch {
            try {
                dao.updateAddress(addressModel)
            }catch (e:Exception){}
        }
    }

    fun getAddress() {
        _address = dao.getAllAddress().asLiveData()
    }

    fun deleteAddress(addressModel: AddressModel) {
        viewModelScope.launch { 
            try {
                dao.deleteAddress(addressModel)
            }catch (e:Exception){}
        }
    }

    fun getContext(): Context {
        return getApplication<Application>().applicationContext
    }
}

class ViewModelFactory(private val dao: AppDao, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShoppingViewModel::class.java)) {
            return ShoppingViewModel(dao, application) as T
        }
        throw IllegalStateException("Unknown")
    }
}