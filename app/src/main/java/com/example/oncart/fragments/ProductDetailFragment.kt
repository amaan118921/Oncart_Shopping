package com.example.oncart.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.application.MyApplication
import com.example.oncart.helper.*
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import com.example.oncart.model.ProductModel
import com.example.oncart.networkServices.Result
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.network_error.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment: BaseFragment() {

    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(requireActivity(),(requireActivity().application as MyApplication).database.getDao())
    }

    @Inject
    lateinit var repo: HelpRepo

    private var productItem: ProductItems? = null
    override fun getLayout(): Int {
        return R.layout.fragment_product_detail
    }
    private var isLoggedIn = false
    private var cartList = listOf<ProductItems>()
    private var favList = listOf<LikedItems>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productItem = arguments?.getParcelable(Constants.PRODUCT_ITEM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
           popBackStack()
        }
        isLoggedIn = repo.getSharedPreferences(Constants.PHONE)!=""
        initView()
        ivBack.makeVisible()
        badge.makeGone()
        getCart()
        getLiked()
        setObservers()
        btnRetry.setOnClickListener(this)
        btnAddToCart.setOnClickListener(this)
        ivFav.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    private fun setObservers() {
        viewModel._likedList.observe(viewLifecycleOwner) {
            favList = it
            hideProgressFrame()
            checkForFavUI()
        }
        viewModel._cartList.observe(viewLifecycleOwner) {
            if(it!=null) {
                cartList = it
                checkForCartUI()
                hideProgressFrame()
            }else {
                cartList = emptyList()
                hideProgressFrame()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkForFavUI() {
        if(!(favList.contains(productItem?.default()?.toLikedItems()))) {
            ivFav.setImageDrawable(resources.getDrawable(R.drawable.heart__4_))
        }else {
           ivFav.setImageDrawable(resources.getDrawable(R.drawable.heart__5_))
        }
        ivFav.makeVisible()
    }

    private fun getLiked() {
        viewModel.getLikedItems()
        hideNetworkFrame()
        showProgressFrame()
    }

    private fun checkForCartUI() {
        cartList.forEach{
            if(productItem?.id==it.id){btnAddToCart.text = getString(R.string.remove_from_cart)
            return
            }
        }
        btnAddToCart.text = getString(R.string.add_to_cart)
    }

    private fun getCart() {
        viewModel.getCartItems()
        hideNetworkFrame()
        showProgressFrame()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initView() {
        productItem?.let { Picasso.get().load(it.imageUrl).into(ivProductDetail)}
        tvProductName.text = productItem?.id
        tvPrice.text = productItem?.price
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.ivBack -> {
                popBackStack()
            }
            R.id.ivFav -> {
                checkForFav()
            }
            R.id.btnAddToCart -> {
                checkForCart()
            }
            R.id.btnRetry -> {
                reload()
            }
        }
    }

    private fun reload() {
        getCart()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkForFav() {
        if(isLoggedIn) {
            if(!(favList.contains(productItem?.default()?.toLikedItems()))) {
                productItem?.let { viewModel.addToLiked(it.default().toLikedItems())
                    Utils.showToast(requireActivity(), "Added to Liked")
                }
            }else {
                productItem?.let { viewModel.removeFromLiked(it.default().toLikedItems())
                    Utils.showToast(requireActivity(), "Remove from Liked")
                }
            }
        } else Utils.showToast(requireActivity(), "Login to continue")
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkForCart() {
        if(isLoggedIn){
            cartList.forEach{ productItems ->
                if(productItem?.id==productItems.id){productItem?.let {
                    viewModel.removeFromCart(it)
                    Utils.showToast(requireActivity(), "Removed from cart")
                }
                    return
                }
            }
            productItem?.apply {
                this.quantity = 1
                viewModel.addToCart(this)
                Utils.showToast(requireActivity(), "Added to cart")
            }
        }else Utils.showToast(requireActivity(), "Login to continue")
    }

    private fun showNetworkFrame() {
        neDetail.makeVisible()
    }

    private fun hideNetworkFrame() {
        neDetail.makeGone()
    }

    private fun showProgressFrame() {
        pfDetail.makeVisible()
    }

    private fun hideProgressFrame() {
        pfDetail.makeGone()
    }

}

