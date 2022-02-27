package com.example.oncart.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.activities.ShoppingActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_login
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            finish()
        }
        btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnLogin -> {
                startShoppingActivity()
            }
        }
    }
    private fun startShoppingActivity() {
        startActivity(Intent(requireContext(), ShoppingActivity::class.java))
        finish()
    }
}