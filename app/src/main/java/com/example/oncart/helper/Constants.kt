package com.example.oncart.helper

import androidx.fragment.app.Fragment
import com.example.oncart.R
import com.example.oncart.bottomSheet.ConfirmAndPay
import com.example.oncart.fragments.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class Constants {
    companion object {
        const val ERROR_MSG = "Something went wrong, try again later..."
        const val HOME_LABEL = "HomeFragment"
        const val BASE_URL = "https://oncart-api-backend.herokuapp.com/"
        const val PRODUCT_DETAIL_FRAGMENT = R.id.productDetailFragment
        const val PRODUCT_ITEM = "product_item"
        const val PRODUCT_DETAIL = "product_detail"
        const val PHONE = "phone"
        const val TOTAL = "TOTAL"
        const val CART_ID = "cart_id"
        const val APP_NAME = "oncart"
        const val CART = "cart"
        const val CART_ = 11
        const val FAVORITE_ = 10
        const val CARD_DRAWABLE = "CARD_DRAWABLE"
        const val CHECKOUT_ID = "checkout_id"
        const val GOOGLE_REQUEST_CODE = 157
        const val LOGGED_IN = "logged_in"
        const val STARTING_SCREEN_FRAGMENT_ID = "starting_screen_fragment_id"
        const val LOGIN_ID = "login_id"
        const val PROFILE_ID = "profile_id"
        const val OTP_ID = "otp_id"
        const val CONFIRM_AND_PAY_ID = "confirm_and_pay"
        fun getFragmentClass(id: String): Class<Fragment> {
            return when(id) {
                PRODUCT_DETAIL -> ProductDetailFragment::class.java as Class<Fragment>
                CART_ID -> CartFragment::class.java as Class<Fragment>
                LOGIN_ID -> LoginFragment::class.java as Class<Fragment>
                PROFILE_ID -> ProfileFragment::class.java as Class<Fragment>
                CHECKOUT_ID -> CheckoutFragment::class.java as Class<Fragment>
                STARTING_SCREEN_FRAGMENT_ID -> StartingFragment::class.java as Class<Fragment>
                OTP_ID -> OTPFragment::class.java as Class<Fragment>
                else -> HomeFragment::class.java as Class<Fragment>
            }
        }

        fun getIdByFragment(frag: Fragment): String {
            return when(frag) {
                OTPFragment() -> OTP_ID
                CheckoutFragment() -> CHECKOUT_ID
                CartFragment() -> CART_ID
                ProductDetailFragment() -> PRODUCT_DETAIL
                else -> OTP_ID
            }
        }

        fun getBottomSheet(id: String): BottomSheetDialogFragment {
            return when(id) {
                CONFIRM_AND_PAY_ID -> ConfirmAndPay()
                else -> ConfirmAndPay()
            }
        }

    }
}