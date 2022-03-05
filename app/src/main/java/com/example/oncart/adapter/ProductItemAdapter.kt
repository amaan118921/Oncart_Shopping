package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.model.ProductItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_products.view.*
import kotlinx.android.synthetic.main.tv_item_view.view.*

class ProductItemAdapter(private val listener: Listener, private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Listener {
        fun onProductItemClick(product: ProductItems)
    }

    var list = arrayListOf<ProductItems>()
    var code = 1
    @SuppressLint("NotifyDataSetChanged")
    fun bindList(list: ArrayList<ProductItems>) {
        this.list = list
        notifyDataSetChanged()
    }
    inner class ProductItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvProductName = view.tvItemName
        var ivProduct = view.ivItem
        var tvPrice = view.tvPrice
    }

    inner class TeleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvName = view.tvTVId
        var tvImage = view.ivTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==0) {
            TeleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tv_item_view, parent, false))
        }else ProductItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_products, parent, false))
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass==TeleViewHolder::class.java) {
            var teleHolder = holder as TeleViewHolder
            list[position].let {
                teleHolder.apply {
                    tvName.text = it.id
                    Picasso.get().load(it.imageUrl).into(tvImage)
                }
            }
        }else {
            var productHolder = holder as ProductItemViewHolder
            list[position].let {
                productHolder.apply {
                    tvProductName.text = it.id
                    Picasso.get().load(it.imageUrl).into(ivProduct)
                    tvPrice.text = it.price
                    itemView.setOnClickListener {
                        listener.onProductItemClick(list[position])
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }
}