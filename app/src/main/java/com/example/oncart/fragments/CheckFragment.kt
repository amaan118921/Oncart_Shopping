package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import com.example.oncart.R
import com.example.oncart.activities.ShoppingActivity
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckFragment: BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_check
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onClick(p0: View?) {

    }
}