package com.example.oncart.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.ProductItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class ProductDetailFragment: BaseFragment() {

    private var productItem: ProductItems? = null
    override fun getLayout(): Int {
        return R.layout.fragment_product_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productItem = arguments?.getParcelable(Constants.PRODUCT_ITEM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
            removeFragment()
        }
        initView()
        bottomGone()
        ivBack.makeVisible()
        badge.makeGone()
        ivFav.makeVisible()
        ivFav.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomVisible()
    }

    private fun initView() {
        productItem?.let { Picasso.get().load(it.imageUrl).into(ivProductDetail)}
        tvProductName.text = productItem?.name
        tvPrice.text = productItem?.price
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.ivBack -> {
                removeFragment()
            }
            R.id.ivFav -> {
                checkForCart()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkForCart() {
        ivFav.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_24))
        Utils.showToast(requireActivity(), getString(R.string.cart_success))
    }

}