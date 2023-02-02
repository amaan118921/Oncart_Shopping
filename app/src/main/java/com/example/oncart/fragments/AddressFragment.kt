package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.adapter.AddressAdapter
import com.example.oncart.application.MyApplication
import com.example.oncart.eventBus.MessageEvent
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.AddressModel
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment__address.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment: BaseFragment(), AddressAdapter.Listener {

    override fun getLayout(): Int {
        return R.layout.fragment__address
    }
    private var addressList = listOf<AddressModel>()
    private var adapter: AddressAdapter? = null
    private var requiredColor: Int? = null
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(
            (requireActivity().application as MyApplication).database.getDao(), (requireActivity().application as MyApplication)
        )
    }
    @Inject
    lateinit var repo: HelpRepo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            notifyAndPop()
        }
        badge.makeGone()
        tvCentre.makeVisible()
        tvCentre.text = "Shipping Addresses"
        ivBack.makeVisible()
        setRecyclerView()
        viewModel.getAddress()
        setObserver()
        btnAddAddress.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    private fun checkForButton(size: Int) {
        requiredColor = if(size==3) {
            resources.getColor(R.color.app_blue_fade)
        }else {
            resources.getColor(R.color.app_blue)
        }
        btnAddAddress.makeVisible()
        requiredColor?.let { btnAddAddress.setBackgroundColor(it) }
    }

    private fun notifyAndPop() {
        EventBus.getDefault().post(MessageEvent(getString(R.string.check_for)))
        popBackStack()
    }

    private fun setRecyclerView() {
        adapter = AddressAdapter(requireActivity(), this, repo)
        rvAddress.adapter = adapter
        rvAddress.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setObserver() {
        viewModel._address.observe(viewLifecycleOwner) {
            addressList = it
            checkForList(it)
            adapter?.bindList(it)
            checkForButton(it.size)
        }
    }

    private fun checkForList(it: List<AddressModel>?) {
        if(it?.isNotEmpty()==true) {
            tvNoAdd.makeGone()
        } else tvNoAdd.makeVisible()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.ivBack -> notifyAndPop()
            R.id.btnAddAddress -> {
                when(requiredColor) {
                    resources.getColor(R.color.app_blue_fade) -> Utils.showToast(requireActivity(), "You have already added three addresses")
                    else -> toAddAddressFragment()
                }
            }
        }
    }
    private fun toAddAddressFragment() {
        addFragment(Constants.ADD_ADDRESS_ID, null)
    }

    override fun onItemClick(addressModel: AddressModel) {
        save(addressModel)
    }

    override fun onDelete(addressModel: AddressModel) {
            showBottom(addressModel)
    }

    private fun showBottom(addressModel: AddressModel) {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.clear_cart_bottom)
        val tv = bottomSheet.findViewById<MaterialTextView>(R.id.tvClearCart)
        tv?.text = "Delete Address?"
        val clearBtn = bottomSheet.findViewById<MaterialButton>(R.id.btnClearCart)
        clearBtn?.text = "Delete"
        clearBtn?.setOnClickListener {
            checkForSavedAddress(addressModel)
            viewModel.deleteAddress(addressModel)
            bottomSheet.dismissWithAnimation = true
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }

    private fun checkForSavedAddress(addressModel: AddressModel) {
        addressModel.apply {
            if(address==repo.getSharedPreferences(Constants.SELECTED_ADDRESS) && name == repo.getSharedPreferences(Constants.SELECTED_NAME) &&
              phone == repo.getSharedPreferences(Constants.SELECTED_PHONE)) {
                deleteSavedAddress()
            }
        }
    }

    private fun deleteSavedAddress() {
        repo.deleteSharedPreferences(Constants.SELECTED_PHONE)
        repo.deleteSharedPreferences(Constants.SELECTED_NAME)
        repo.deleteSharedPreferences(Constants.SELECTED_ADDRESS)
        repo.deleteSharedPreferences(Constants.SELECTED_ADDRESS_CODE)
    }

    private fun save(addressModel: AddressModel) {
        addressModel.apply {
            repo.setSharedPreferences(Constants.SELECTED_ADDRESS, address)
            repo.setSharedPreferences(Constants.SELECTED_NAME, name)
            repo.setSharedPreferences(Constants.SELECTED_PHONE, phone)
        }
    }
}