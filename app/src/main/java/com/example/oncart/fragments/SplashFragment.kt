package com.example.oncart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.oncart.R
import com.example.oncart.helper.Constants


class SplashFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_splash
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.myLooper()!!).postDelayed({
            removeFragment(Constants.SPLASH_ID)
            initViewPager()
        }, 3000)
    }

    override fun onClick(view: View?) {

    }
}