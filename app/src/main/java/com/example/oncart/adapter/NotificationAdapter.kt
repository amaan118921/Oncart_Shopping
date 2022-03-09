package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.model.ProductItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_notification.view.*

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var list = arrayListOf<ProductItems>()

    @SuppressLint("NotifyDataSetChanged")
    fun bindList(list: ArrayList<ProductItems>) {
        this.list = list
        notifyDataSetChanged()
    }
    class NotificationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val img = view.ivNotification
        val name = view.tvNotificationItemName
        val price = view.tvNotificationPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        list[position].let {
            holder.apply {
                name.text = it.id
                price.text = it.price
                Picasso.get().load(it.imageUrl).into(img)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}