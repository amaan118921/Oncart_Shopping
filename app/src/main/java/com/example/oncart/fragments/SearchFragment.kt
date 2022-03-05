package com.example.oncart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.activities.ShoppingActivity
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_search
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
                }, 2000)
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {

        }
    }


}