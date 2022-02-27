package com.example.oncart.helper

import androidx.fragment.app.Fragment
import com.example.oncart.R
import com.example.oncart.fragments.HomeFragment
import com.example.oncart.fragments.ProductDetailFragment

class Constants {
    companion object {
        const val ERROR_MSG = "Something went wrong, try again later..."
        const val LOGIN_NAV_ID = R.id.loginFragment
        const val HOME_LABEL = "HomeFragment"
        const val BASE_URL = "https://oncart-api-backend.herokuapp.com/"
        const val PRODUCT_DETAIL_FRAGMENT = R.id.productDetailFragment
        const val PRODUCT_ITEM = "product_item"
        const val PRODUCT_DETAIL = "product_detail"
    }
}