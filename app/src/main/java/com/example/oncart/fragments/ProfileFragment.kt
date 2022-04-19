package com.example.oncart.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.eventBus.MessageEvent
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_profile
    }

    @Inject
    lateinit var repo: HelpRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!(EventBus.getDefault().isRegistered(this))) EventBus.getDefault().register(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
            if (finish) {
                finish()
            } else {
                finish = true
                Utils.showToast(requireActivity(), "Press again to exit...")
                Handler(Looper.myLooper()!!).postDelayed({
                    finish = false
                }, 3000)
            }
        }
        initView()
        cvAddress.setOnClickListener(this)
        cvMyCart.setOnClickListener(this)
        cvEdit.setOnClickListener(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {
        repo.getSharedPreferences(Constants.AVATAR_ID).toIntOrNull()
            ?.let { ivProfile.setImageDrawable(requireContext().getDrawable(it)) }
        tvName.text = repo.getSharedPreferences(Constants.NAME)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cvMyCart -> {
                addFragment(Constants.CART_ID, null)
            }
            R.id.cvEdit -> {
                Bundle().apply {
                    putBoolean(Constants.EDIT_MODE, true)
                    addFragment(Constants.PROFILE_DIALOG_ID, this)
                }
            }
            R.id.cvAddress -> addFragment(Constants.ADDRESS_ID, null)
        }
    }


    @Subscribe
    fun onMessageEvent(event: MessageEvent) {
        when (event.getString()) {
            getString(R.string.update_profile) -> initView()
        }
    }
}