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
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.order_placed_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_checkout
    }
    private var toPopBackStack = true
    @Inject
    lateinit var repo: HelpRepo

    private var totalCost = "0.00"
    private var totalQuantity = 0
    private var isShippingAdded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!(EventBus.getDefault().isRegistered(this)))EventBus.getDefault().register(this)
        totalCost = arguments?.getString(Constants.TOTAL)?:"0.00"
        totalQuantity = arguments?.getInt(Constants.TOTAL_QUANTITY)?:0
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(toPopBackStack) {popBackStack()}
        }
        rb1.isChecked = true
        tvPhone.text = "+91 ${repo.getSharedPreferences(Constants.PHONE)}"
        badge.makeGone()
        tvCentre.text = "Checkout"
        tvCentre.makeVisible()
        ivBack.makeVisible()
        tvTotalValue.text = totalCost
        tvName.text = repo.getSharedPreferences(Constants.NAME)
        btnConfirmAndPay.setOnClickListener(this)
        tvChange.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        shopMore.setOnClickListener(this)
        checkForAddress()
    }

    private fun checkForAddress() {
        repo.apply {
            val name = getSharedPreferences(Constants.SELECTED_NAME)
            val address = getSharedPreferences(Constants.SELECTED_ADDRESS)
            val phone = getSharedPreferences(Constants.SELECTED_PHONE)
            if(name.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty()) {
                noShipInfo.makeGone()
                containerShipInfo.makeVisible()
                initView(name, address, phone)
                isShippingAdded = true
            }else {
                isShippingAdded = false
                noShipInfo.makeVisible()
                containerShipInfo.makeGone()
            }
        }
    }

    private fun initView(name: String, address: String, phone: String) {
        tvName.text = name
        tvAddress.text = "Address: $address"
        tvPhone.text = "+91 $phone"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onMessageEvent(event: MessageEvent) {
        when(event.getString()) {
            getString(R.string.transaction) -> {
                executeTransaction()
            }
            getString(R.string.check_for) -> checkForAddress()
        }
    }

    private fun executeTransaction() {
        EventBus.getDefault().post(MessageEvent(getString(R.string.clear_cart)))
        toPopBackStack = false
        transactionLayout.makeVisible()
        Handler(Looper.myLooper()!!).postDelayed({
            orderPlacedLayout.makeVisible()
        }, 3000)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                popBackStack()
            }
            R.id.btnConfirmAndPay -> {
                if(isShippingAdded) checkAndShow()
                else Utils.showToast(requireActivity(), "Add shipping address")
            }
            R.id.shopMore -> {
                navigateToHome()
            }
            R.id.tvChange -> addFragment(Constants.ADDRESS_ID, null)
        }
    }

    private fun navigateToHome() {
        popBackStack()
        popBackStack()
        gotToHome()
    }

    private fun checkAndShow() {
        when (rGroup.checkedRadioButtonId) {
            R.id.rb1 -> {
                Bundle().apply {
                    putInt(Constants.TOTAL_QUANTITY, totalQuantity)
                    putString(Constants.TOTAL, totalCost)
                    putInt(Constants.CARD_DRAWABLE, R.drawable.visa)
                    showBottomSheet(Constants.CONFIRM_AND_PAY_ID, this)
                }
            }
            R.id.rb2 -> {
                Bundle().apply {
                    putInt(Constants.TOTAL_QUANTITY, totalQuantity)
                    putString(Constants.TOTAL, totalCost)
                    putInt(Constants.CARD_DRAWABLE, R.drawable.maestro)
                    showBottomSheet(Constants.CONFIRM_AND_PAY_ID, this)
                }
            }
            R.id.rb3 -> {
                Bundle().apply {
                    putInt(Constants.TOTAL_QUANTITY, totalQuantity)
                    putString(Constants.TOTAL, totalCost)
                    putInt(Constants.CARD_DRAWABLE, R.drawable.credit_card)
                    showBottomSheet(Constants.CONFIRM_AND_PAY_ID, this)
                }
            }
        }
    }
}