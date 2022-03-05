package com.example.oncart.bottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oncart.R
import com.example.oncart.helper.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.confirm_and_pay_bottom.*

class ConfirmAndPay: BottomSheetDialogFragment() {

    private var intDrawable = R.drawable.visa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intDrawable = arguments?.getInt(Constants.CARD_DRAWABLE)?:R.drawable.visa
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.confirm_and_pay_bottom, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivCard.setImageDrawable(resources.getDrawable(intDrawable))
        isCancelable = true
    }
}