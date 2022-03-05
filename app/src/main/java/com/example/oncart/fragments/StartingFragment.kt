package com.example.oncart.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.oncart.R
import com.example.oncart.adapter.IndicatorAdapter
import com.example.oncart.adapter.StartingScreenAdapter
import com.example.oncart.helper.Constants
import com.example.oncart.helper.ScrollListenerHelper
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.StartingScreenModel
import kotlinx.android.synthetic.main.fragment_starting.*

class StartingFragment: BaseFragment(), ScrollListenerHelper.OnSnapPositionChangeListener {

    private var pagerSnapHelper: PagerSnapHelper? = null
    private var scrollListenerHelper: ScrollListenerHelper? = null
    private var adapter: StartingScreenAdapter?  = null
    private var snapPosition = 0
    override fun getLayout(): Int {
        return R.layout.fragment_starting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerViewWithPager()
        btnGetStarted.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnGetStarted -> {
                replaceFragment(Constants.LOGIN_ID, null)
            }
         }
    }

    private fun setUpRecyclerViewWithPager() {
        pagerSnapHelper = PagerSnapHelper()
        adapter = StartingScreenAdapter(requireContext())
        adapter?.bindView(getList())
        rvStarting.apply {
            layoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            pagerSnapHelper?.attachToRecyclerView(this)
            adapter = this@StartingFragment.adapter
        }
        pagerSnapHelper?.let { scrollListenerHelper = ScrollListenerHelper(this, it, snapPosition) }
        scrollListenerHelper?.let { rvStarting.addOnScrollListener(it) }
    }

    private fun getList(): ArrayList<StartingScreenModel> {
        val startModelOne = StartingScreenModel(getString(R.string.find_your_ngadget), R.drawable.resource_new)
        val startModelTwo = StartingScreenModel(getString(R.string.get_your_fav_clothes),  R.drawable.clothe)
        val startModelThree = StartingScreenModel(getString(R.string.get_started_with_onCart), R.drawable.carts)
        return arrayListOf(startModelOne, startModelTwo, startModelThree)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSnapPositionChange(position: Int) {
        if(position==2) {btnGetStarted.makeVisible()} else btnGetStarted.makeGone()
        manipulateDot(position)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun manipulateDot(position: Int) {
        when(position) {
            0 -> {
                dot1.background = resources.getDrawable(R.drawable.selected_dot)
                dot2.background = resources.getDrawable(R.drawable.default_dot)
                dot3.background = resources.getDrawable(R.drawable.default_dot)
            }
            1 -> {
                dot2.background = resources.getDrawable(R.drawable.selected_dot)
                dot1.background = resources.getDrawable(R.drawable.default_dot)
                dot3.background = resources.getDrawable(R.drawable.default_dot)
            }
            2 -> {
                dot3.background = resources.getDrawable(R.drawable.selected_dot)
                dot1.background = resources.getDrawable(R.drawable.default_dot)
                dot2.background = resources.getDrawable(R.drawable.default_dot)
            }
        }
    }



}