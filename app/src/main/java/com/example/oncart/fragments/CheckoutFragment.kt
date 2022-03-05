package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.example.oncart.R
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.toolbar.*

class CheckoutFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_checkout
    }
    private var totalCost = "0.00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        totalCost = arguments?.getString(Constants.TOTAL)?:"0.00"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            popBackStack()
        }
        badge.makeGone()
        tvCentre.text = "Checkout"
        tvCentre.makeVisible()
        ivBack.makeVisible()
        tvTotalValue.text = totalCost
        btnConfirmAndPay.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                popBackStack()
            }
            R.id.btnConfirmAndPay -> {
                checkAndShow()

            }
        }
    }

    private fun checkAndShow() {
        when (rGroup.checkedRadioButtonId) {
            R.id.rb1 -> {
                Bundle().apply {
                    putInt(Constants.CARD_DRAWABLE, R.drawable.visa)
                    showBottomSheet(Constants.CONFIRM_AND_PAY_ID, this)
                }
            }
            R.id.rb2 -> {
                Bundle().apply {
                    putInt(Constants.CARD_DRAWABLE, R.drawable.maestro)
                    showBottomSheet(Constants.CONFIRM_AND_PAY_ID, this)
                }
            }
            R.id.rb3 -> {
                Bundle().apply {
                    putInt(Constants.CARD_DRAWABLE, R.drawable.credit_card)
                    showBottomSheet(Constants.CONFIRM_AND_PAY_ID, this)
                }
            }
        }
    }
}