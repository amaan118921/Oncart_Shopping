package com.example.oncart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.Utils.Utils.Companion.toListOfProductItems
import com.example.oncart.adapter.CartAdapter
import com.example.oncart.application.MyApplication
import com.example.oncart.helper.*

import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_liked.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class LikedFragment : BaseFragment(), CartAdapter.Listener {
    override fun getLayout(): Int {
        return R.layout.fragment_liked
    }
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(requireActivity(),(requireActivity().application as MyApplication).database.getDao())
    }

    private var cartAdapter: CartAdapter? = null

    private var favList = listOf<LikedItems>()
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
        cartAdapter = CartAdapter(Constants.FAVORITE_, null)
        rvFav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvFav.adapter = cartAdapter
        getLiked()
        setObservers()
        tvCentre.makeVisible()
        tvCentre.text = "Favorites"
        badge.makeGone()
    }

    private fun getLiked() {
        viewModel.getLikedItems()
        hideNetworkFrame()
        showProgressFrame()
    }
    private fun setObservers() {
        viewModel._likedList.observe(viewLifecycleOwner) {
            favList = it
            checkForEmptyFav(it)
            cartAdapter?.bindList(it.toListOfProductItems())
            hideProgressFrame()
        }
    }

    private fun checkForEmptyFav(arrayList: List<LikedItems>?) {
        if(arrayList?.isEmpty()==true) {
            tvEmptyFav.makeVisible()
        }else {
            tvEmptyFav.makeGone()
        }
    }

    override fun onClick(p0: View?) {

    }

    private fun hideNetworkFrame() {

    }

    private fun showProgressFrame() {
        pfFav.makeVisible()
    }

    private fun hideProgressFrame() {
        pfFav.makeGone()
    }

    override fun onIncrementQuantity(productItem: ProductItems) {

    }

    override fun onDecrementQuantity(productItem: ProductItems) {

    }

    override fun onDeleteItem(productItem: ProductItems) {
        viewModel.removeFromLiked(productItem.default().toLikedItems())
    }

}