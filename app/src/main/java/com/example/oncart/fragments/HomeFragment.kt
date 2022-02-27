package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.Utils.Utils
import com.example.oncart.adapter.ProductFilterAdapter
import com.example.oncart.adapter.ProductItemAdapter
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.ProductItems
import com.example.oncart.model.StartingScreenModel
import com.example.oncart.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.network_error.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.progress_frame.*
import kotlinx.android.synthetic.main.toolbar.*


class HomeFragment: BaseFragment(), ProductFilterAdapter.IListener, ProductItemAdapter.Listener {

    private var filterAdapter: ProductFilterAdapter? = null
    private var productAdapter: ProductItemAdapter? = null
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(requireActivity())
    }
    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
            finish()
        }
        bottomVisible()
        btnRetry.setOnClickListener(this)
        setUpRecyclerView()
        tvHeader.makeVisible()
        setUpProductRV()
        setObserver()
        getData(getString(R.string.mobile))
        tvHeader.text = getString(R.string.welcome)
    }

    private fun getData(product: String) {
        showProgressFrame()
        viewModel.getMobileProducts(product)
    }

    private fun showProgressFrame() {
        pfHome.makeVisible()
    }

    private fun hideProgressFrame() {
        pfHome.makeGone()
    }

    private fun setObserver() {
        viewModel._list.observe(viewLifecycleOwner) {
            hideProgressFrame()
            productAdapter?.bindList(it, )
        }
    }

    private fun setUpProductRV() {
        productAdapter = ProductItemAdapter(this)
        rvProducts.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        rvProducts.adapter = productAdapter
    }

    private fun setUpRecyclerView() {
        filterAdapter = ProductFilterAdapter(requireContext(), this)
        rvProductFilter.adapter = filterAdapter
        rvProductFilter.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterAdapter?.bindView(getList())
    }

    private fun getList(): ArrayList<StartingScreenModel> {
        return arrayListOf(StartingScreenModel(getString(R.string.mobiles), R.drawable.iphone__1_),
            StartingScreenModel(getString(R.string.laptops),R.drawable.laptop), StartingScreenModel(getString(R.string.Televisions), R.drawable.television),
            StartingScreenModel(getString(R.string.men_top), R.drawable.jacket), StartingScreenModel(getString(R.string.men_bottom), R.drawable.jeans)
            )
    }


    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnRetry -> {
                loadMobileData()
            }
        }
    }

    private fun loadMobileData() {
        getData(getString(R.string.mobile))
        neHome.makeGone()
    }

    override fun onFilterItemClick(title: String) {
        when(title) {
            getString(R.string.mobiles) -> getData(getString(R.string.mobile))
            getString(R.string.Televisions) -> getData(getString(R.string.Television))
            getString(R.string.men_top) -> getData(getString(R.string.men_top_wear))
            getString(R.string.men_bottom) -> getData(getString(R.string.men_bottom_wear))
            else -> getData(title)
        }
    }

    override fun onProductItemClick(product: ProductItems) {
        toDetailFragment(product)
    }

    private fun toDetailFragment(product: ProductItems) {

        Bundle().apply {
            putParcelable(Constants.PRODUCT_ITEM, product)
            addFragment(Constants.PRODUCT_DETAIL, this)
        }
    }
}