package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.example.oncart.model.AddressModel
import kotlinx.android.synthetic.main.item_view_address.view.*

class AddressAdapter(private val activity: Activity, private val listener: Listener, private val repo: HelpRepo): RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    private var addressList = listOf<AddressModel>()

    private var prevChecked = 0
    interface Listener {
        fun onItemClick(addressModel: AddressModel)
        fun onDelete(addressModel: AddressModel)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun bindList(addressList: List<AddressModel>) {
        this.addressList = addressList
        notifyDataSetChanged()
    }
    class AddressViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_address, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        addressList[position].let {
            holder.itemView.apply {
                cBox.isChecked = checkForSelected(position)
                tvName.text = it.name
                tvAddress.text = "Address: ${it.address}"
                tvPhone.text = "+91 ${it.phone}"
                cvShipInfo.setOnClickListener {
                    if(!checkForSelected(position)) {
                        listener.onItemClick(addressList[position])
                        repo.setSharedPreferences(Constants.SELECTED_ADDRESS_CODE, position.toString())
                        notifyDataSetChanged()
                    }
                }
                cvShipInfo.setOnLongClickListener {
                    listener.onDelete(addressList[position])
                    return@setOnLongClickListener true
                }
            }
        }
    }

    private fun checkForSelected(position: Int): Boolean {
        return position==repo.getSharedPreferences(Constants.SELECTED_ADDRESS_CODE).toIntOrNull()?:-1
    }
    override fun getItemCount(): Int {
        return addressList.size
    }
}