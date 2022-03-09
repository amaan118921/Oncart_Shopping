package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.adapter.NotificationAdapter
import com.example.oncart.application.MyApplication
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.toolbar.*

class NotificationFragment: BaseFragment() {
    override fun getLayout(): Int {
        return R.layout.fragment_notification
    }
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(requireActivity(),(requireActivity().application as MyApplication).database.getDao())
    }

    private var notificationAdapter: NotificationAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
            popBackStack()
        }
        ivBack.makeVisible()
        tvCentre.text = "Notifications"
        tvCentre.makeVisible()
        badge.makeGone()
        setObserver()
        ivBack.setOnClickListener {
            popBackStack()
        }
        notificationAdapter = NotificationAdapter()
        rvNotification.adapter = notificationAdapter
        rvNotification.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.getNotification()
    }

    override fun onClick(p0: View?) {

    }

    private fun setObserver() {
        viewModel._notification.observe(viewLifecycleOwner) {
            notificationAdapter?.bindList(it)
        }
    }

}