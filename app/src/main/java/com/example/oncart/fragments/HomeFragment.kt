package com.example.oncart.fragments

import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.adapter.ProductFilterAdapter
import com.example.oncart.adapter.ProductItemAdapter
import com.example.oncart.application.MyApplication
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.ProductItems
import com.example.oncart.model.StartingScreenModel
import com.example.oncart.reciever.ConnectionReceiver
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.network_error.*
import kotlinx.android.synthetic.main.notification_badge.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment(), ProductFilterAdapter.IListener, ProductItemAdapter.Listener,
    ConnectionReceiver.ReceiverListener {

    private var filterAdapter: ProductFilterAdapter? = null
    private var productAdapter: ProductItemAdapter? = null
    private var isReceiverRegistered = false
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(
            requireActivity(),
            (requireActivity().application as MyApplication).database.getDao()
        )
    }

    @Inject
    lateinit var repo: HelpRepo

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }


    private var isLoggedIn = false
    private var scope: LifecycleCoroutineScope? = null
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
                }, 2000)
            }
        }
        isLoggedIn =  repo.getSharedPreferences(Constants.PHONE)!=""
        scope = viewLifecycleOwner.lifecycleScope
        bottomVisible()
        btnRetry.setOnClickListener(this)
        setUpRecyclerView()
        tvHeader.makeVisible()
        tvBadge.makeGone()
        flBadge.setOnClickListener(this)
        setUpProductRV()
        setObserver()
        viewModel.getNotificationProducts()
        getData(getString(R.string.mobile))
        tvHeader.text = getString(R.string.welcome)
        checkForProfile()
    }

    private fun initLooper() {
        scope?.launch {
            Handler(Looper.myLooper()!!).postDelayed({
                checkForInternet()
            }, 2000)
        }
    }

    private fun checkForInternet() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");
        if (!isReceiverRegistered) {
            ConnectionReceiver().apply {
                requireActivity().registerReceiver(this, intentFilter)
                this.listener = this@HomeFragment
            }
        }
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
        viewModel.list.observe(viewLifecycleOwner) {
            hideProgressFrame()
            productAdapter?.bindList(it)
        }
        viewModel._notificationProducts.observe(viewLifecycleOwner) {
//            EventBus.getDefault().post(ProductEvent(getString(R.string.list_of_product_items), it))
        }
    }

    private fun checkForProfile() {
        if(isLoggedIn) {
            if (repo.getSharedPreferences(Constants.LOGGED_IN) != Constants.LOGGED_IN) {
                callProfileDialog()
            }
        }
    }

    private fun callProfileDialog() {
        addFragment(Constants.PROFILE_DIALOG_ID, null)
    }

    private fun setUpProductRV() {
        productAdapter = ProductItemAdapter(this, requireContext())
        rvProducts.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        rvProducts.adapter = productAdapter
    }

    private fun setUpRecyclerView() {
        filterAdapter = ProductFilterAdapter(requireContext(), this)
        rvProductFilter.adapter = filterAdapter
        rvProductFilter.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterAdapter?.bindView(getList())
    }

    private fun getList(): ArrayList<StartingScreenModel> {
        return arrayListOf(
            StartingScreenModel(getString(R.string.mobiles), R.drawable.iphone__1_),
            StartingScreenModel(getString(R.string.laptops), R.drawable.laptop),
            StartingScreenModel(getString(R.string.Televisions), R.drawable.television),
            StartingScreenModel(getString(R.string.men_top), R.drawable.jacket),
            StartingScreenModel(getString(R.string.men_bottom), R.drawable.jeans)
        )
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRetry -> {
                loadMobileData()
            }
            R.id.flBadge -> {
                addFragment(Constants.NOTIFICATION_ID, null)
            }
        }
    }

    private fun loadMobileData() {
        getData(getString(R.string.mobile))
        neHome.makeGone()
    }

    override fun onFilterItemClick(title: String) {
        when (title) {
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

    override fun onNetworkChange(isConnected: Boolean) {
        if (!isConnected) {
            Utils.showToast(requireActivity(), "No internet connection")
        }
    }
}