package com.example.oncart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.helper.Constants
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment: BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
            if(finish) {
                finish()
            }else {
                finish = true
                Utils.showToast(requireActivity(), "Press again to exit...")
                Handler(Looper.myLooper()!!).postDelayed({
                    finish = false
                }, 3000)
            }
        }
        cvMyCart.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.cvMyCart -> {
                addFragment(Constants.CART_ID, null)
            }
        }
    }
}