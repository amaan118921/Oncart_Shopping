package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R

class LikedFragment : BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_liked
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    override fun onClick(p0: View?) {

    }
}