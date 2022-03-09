package com.example.oncart.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.activities.ShoppingActivity
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_otp.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OTPFragment: BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_otp
    }
    private lateinit var userid: String
    private lateinit var dialog: ProgressDialog
    private lateinit var userToken: PhoneAuthProvider.ForceResendingToken
    private var isFromAccount: Boolean? = false

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var repo: HelpRepo

    private var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phone = arguments?.getString(Constants.PHONE)?:""
        isFromAccount = arguments?.getBoolean(Constants.FROM_ACCOUNT)?:false
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
        getCallbacks()
        btnVerify.setOnClickListener(this)
        setTextChanger()
        tvPhoneNumber.text = "+91 $phone"
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnVerify -> validate()
        }
    }

    private fun validate() {
        val otp = ed1.text.toString().trim() + ed2.text.toString().trim() + ed3.text.toString().trim() +
                ed4.text.toString().trim() + ed5.text.toString().trim() + ed6.text.toString().trim()
        if(otp.length<6) {Utils.showToast(requireActivity(), "Enter valid 6 digit otp")}
        else{
            val credential = PhoneAuthProvider.getCredential(userid, otp)
            callDialog()
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun setTextChanger() {
        ed1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (ed1.text.length == 1) {
                    ed2.requestFocus()
                }
            }

        })
        ed2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


            override fun afterTextChanged(s: Editable?) {
                if (ed2.text.length == 1) {
                    ed3.requestFocus()
                }
            }

        })
        ed3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (ed3.text.length == 1) {
                    ed4.requestFocus()
                }
            }

        })
        ed4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (ed4.text.length == 1) {
                    ed5.requestFocus()
                }
            }

        })
        ed5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(ed5.text.length == 1) {
                    ed6.requestFocus()
                }
            }

        })
    }

    private fun getCallbacks() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(requireContext(), "Verification Successful", Toast.LENGTH_SHORT)
                    .show()
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                        .show()
                }
                removeOTP()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                userid = p0
                userToken = p1
            }
        }
        val reqPhone = "+91$phone"
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(reqPhone).setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                dialog.dismiss()
                repo.setSharedPreferences(Constants.PHONE, phone)
                val newUser = p0.result!!.additionalUserInfo!!.isNewUser
                if (newUser) {
                    Utils.showToast(requireActivity(), "Verification Successful")
                } else {
                    Utils.showToast(requireActivity(), "Welcome Back")
                }
                if(isFromAccount==false){
                    removeOTP()
                    initViewPager()
                }else {finish()
                startActivity(Intent(requireActivity(), ShoppingActivity::class.java))}
            } else {
                dialog.dismiss()
                Utils.showToast(requireActivity(), "Incorrect otp")
                removeOTP()
            }
        }
    }

    private fun callDialog() {
        dialog = ProgressDialog(requireContext())
        dialog.setMessage("Please Wait")
        dialog.setCancelable(false)
        dialog.show()
    }

}