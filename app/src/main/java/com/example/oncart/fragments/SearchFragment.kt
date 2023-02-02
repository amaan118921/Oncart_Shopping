package com.example.oncart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.activities.ShoppingActivity
import com.example.oncart.adapter.ProductItemAdapter
import com.example.oncart.adapter.SearchAdapter
import com.example.oncart.application.MyApplication
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.ProductItems
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*

class SearchFragment: BaseFragment(), SearchAdapter.Listener, SearchView.OnQueryTextListener {
    override fun getLayout(): Int {
        return R.layout.fragment_search
    }
    private var addList = arrayListOf<ProductItems>()
    private var productAdapter: SearchAdapter? = null
    private var searchJob: Job? = null
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(
            requireActivity(),
            (requireActivity().application as MyApplication).database.getDao()
        )
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
        setObservers()
        setUpProductRV()
        sv.setOnQueryTextListener(this)
    }

    private fun setObservers() {
        viewModel.listMobile.observe(viewLifecycleOwner) {
            addList(it)
        }
        viewModel.listLaptop.observe(viewLifecycleOwner) {
            addList(it)
        }
        viewModel.listTelevision.observe(viewLifecycleOwner) {
            addList(it)
        }
        viewModel.listTop.observe(viewLifecycleOwner) {
            addList(it)
        }
        viewModel.listBottom.observe(viewLifecycleOwner) {
            addList(it)
        }
    }

    private fun addList(it: ArrayList<ProductItems>?) {
        if (it != null) {
            if(!addList.containsAll(it)) addList.addAll(it)
        }
    }

    private fun setUpProductRV() {
        productAdapter = SearchAdapter(this)
        rvSearch.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        rvSearch.adapter = productAdapter
    }

    override fun onClick(view: View?) {
        when(view?.id) {

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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filterAdapter(newText)
        return true
    }

    private fun filterAdapter(newText: String?) {
        if(newText?.isNotBlank()==true){
            tvResultsSize.makeVisible()
            searchJob?.cancel(null)
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(500)
                val filtered = Utils.getFilteredList(newText, addList)
                productAdapter?.bindList(filtered)
                tvResultsSize.text = "Found ${filtered.size} Results"
            }
        }else {
            productAdapter?.bindList(arrayListOf())
            tvResultsSize.makeGone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sv.setQuery("", false)
        searchJob?.cancel(null)
    }

}