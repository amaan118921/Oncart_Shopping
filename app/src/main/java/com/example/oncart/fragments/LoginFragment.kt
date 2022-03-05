package com.example.oncart.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.activities.ShoppingActivity
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject
@AndroidEntryPoint
class LoginFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_login
    }

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var database: FirebaseDatabase

    @Inject
    lateinit var repo: HelpRepo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            finish()
        }
        btnContinue.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnContinue -> {
                validate()
            }
        }
    }

    private fun validate() {
        val phone = etPhone.text.toString().trim()
        when {
            phone.isEmpty() -> {Utils.showToast(requireActivity(), "Enter 10 digit mobile number")}
            phone.length < 10 -> {Utils.showToast(requireActivity(), "Enter 10 digit mobile number")}
            else -> toOTPFragment(phone)
        }
    }

    private fun toOTPFragment(phone: String) {
        Bundle().apply {
            putString(Constants.PHONE, phone)
            replaceFragment(Constants.OTP_ID, this)
        }
    }

}