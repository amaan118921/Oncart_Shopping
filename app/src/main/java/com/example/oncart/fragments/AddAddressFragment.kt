package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.application.MyApplication
import com.example.oncart.model.AddressModel
import com.example.oncart.viewModel.ShoppingViewModel
import com.example.oncart.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_address.*

class AddAddressFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_add_address
    }
    private val viewModel: ShoppingViewModel by activityViewModels {
        ViewModelFactory(
            requireActivity(),
            (requireActivity().application as MyApplication).database.getDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            popBackStack()
        }
        btnSave.setOnClickListener(this)
        ivClose.setOnClickListener(this)
    }
    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnSave -> validate()
            R.id.ivClose -> popBackStack()
        }
    }

    private fun validate() {
        val name = etName.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        if(name.isEmpty() || address.isEmpty() || phone.isEmpty()) {Utils.showToast(requireActivity(), "Enter all fields")}
        else {
            AddressModel().apply {
                id = System.currentTimeMillis()
                this.address = address
                this.name = name
                this.phone = phone
                viewModel.addAddress(this)
            }
            popBackStack()
        }
    }
}