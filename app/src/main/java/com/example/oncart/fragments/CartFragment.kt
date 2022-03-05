package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.adapter.CartAdapter
import com.example.oncart.application.MyApplication
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.ProductItems
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CartFragment: BaseFragment(), CartAdapter.Listener {
    override fun getLayout(): Int {
        return R.layout.fragment_cart
    }
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(requireActivity(),
            (requireActivity().application as MyApplication).database.getDao())
    }

    private var cartAdapter: CartAdapter? = null
    private var cartList = mutableListOf<ProductItems>()
    var totalCost = 0.0
    var totalCostString = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
          popBackStack()
        }
        cartAdapter = CartAdapter(Constants.CART_, this)
        rvCart.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvCart.adapter = cartAdapter
        getCart()
        setObservers()
        btnCheckout.setOnClickListener(this)
        ivClearCart.makeVisible()
        ivClearCart.setOnClickListener(this)
        ivBack.makeVisible()
        tvCentre.makeVisible()
        badge.makeGone()
    }

    private fun setObservers() {
        viewModel._cart.observe(viewLifecycleOwner) {
            if(it!=null) {
                hideProgressFrame()
                cartList = it
                checkForEmptyCart(it)
                cartAdapter?.bindList(it)
            }else {
                hideProgressFrame()
                checkForEmptyCart(emptyList())
            }
        }
    }

    private fun checkForEmptyCart(arrayList: List<ProductItems>) {
        if(arrayList.isEmpty()) {
            tvEmptyCart.makeVisible()
        }else {
            cartContainer.makeVisible()
            tvEmptyCart.makeGone()
            calculateCosts()
        }
    }

    private fun calculateCosts() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            cartList.forEach {
                totalCost+=getPrice(it.price, it.quantity)
            }
            withContext(Dispatchers.Main) {
                tvPriceValue.makeVisible()
                tvPriceValue.text = "${currencyFormatter(totalCost)}"
            }
        }
    }

    private fun currencyFormatter(num: Double): String {
        val formatter = DecimalFormat("###,###,###")
        totalCostString =  NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(num)
        return totalCostString
    }

    private fun getPrice(price: String, quantity: Int?): Double {
        if(price.contains(",") && price.contains("₹")) {
            val arr = price.removePrefix("₹").split(",")
            if(arr.size!=1) {
                return ((arr[0].trim()+arr[1].trim()).toDouble())* quantity!!
            }
        }
        else if(price.contains("₹")) {
            return (price.removePrefix("₹").toDouble())* quantity!!
        }
        else {
            val arr = price.split(",")
            if(arr.size!=1) {
                return ((arr[0].trim()+arr[1].trim()).toDouble())*quantity!!
            }
        }
        return 0.0
    }

    private fun getCart() {
        showProgressFrame()
        viewModel.getCart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnCheckout -> {
                calculateAndAdd()

            }
            R.id.ivBack -> {
                popBackStack()
            }
            R.id.ivClearCart -> {
                showBottom()
            }
        }
    }

    private fun showBottom() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.clear_cart_bottom)
        val clearBtn = bottomSheet.findViewById<MaterialButton>(R.id.btnClearCart)
        clearBtn?.setOnClickListener {
            viewModel.clearCart()
            bottomSheet.dismissWithAnimation = true
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }

    private fun calculateAndAdd() {
        Bundle().apply {
            putString(Constants.TOTAL, totalCostString)
            addFragment(Constants.CHECKOUT_ID, this)
        }
    }

    private fun showProgressFrame() {
        pfCart.makeVisible()
    }

    private fun hideProgressFrame() {
        pfCart.makeGone()
    }

    override fun onIncrementQuantity(productItem: ProductItems) {
        totalCost = 0.0
        viewModel.increaseQuantity(productItem)
    }

    override fun onDecrementQuantity(productItem: ProductItems) {
        totalCost = 0.0
        viewModel.decreaseQuantity(productItem)
    }

}