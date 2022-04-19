package com.example.oncart.bottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oncart.R
import com.example.oncart.eventBus.MessageEvent
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.confirm_and_pay_bottom.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject
@AndroidEntryPoint
class ConfirmAndPay: BottomSheetDialogFragment(), View.OnClickListener {

    private var intDrawable = R.drawable.visa
    private var totalCost = "0.00"
    private var totalQuantity = 0

    @Inject
    lateinit var repo: HelpRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intDrawable = arguments?.getInt(Constants.CARD_DRAWABLE)?:R.drawable.visa
        totalCost = arguments?.getString(Constants.TOTAL)?:"0.00"
        totalQuantity = arguments?.getInt(Constants.TOTAL_QUANTITY)?:0
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
        tvProductsValue.text = totalQuantity.toString()
        tvPriceValue.text = totalCost
        isCancelable = true
        tvNamePayAndNow.text = repo.getSharedPreferences(Constants.NAME)
        btnPayNow.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnPayNow -> {
                EventBus.getDefault().post(MessageEvent(getString(R.string.transaction)))
                dismiss()
            }
        }
    }
}