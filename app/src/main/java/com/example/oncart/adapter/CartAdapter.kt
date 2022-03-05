package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.example.oncart.model.CartModel
import com.example.oncart.model.ProductItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_cart.view.*


class CartAdapter(private var key: Int, private val listener: Listener?): ListAdapter<ProductItems, CartAdapter.CartViewHolder>(DiffCallback) {

    interface Listener {
        fun onIncrementQuantity(productItem: ProductItems)
        fun onDecrementQuantity(productItem: ProductItems)
    }
    private var list = listOf<ProductItems>()

    @SuppressLint("NotifyDataSetChanged")
    fun bindList(list: List<ProductItems>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class CartViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName = view.tvCartItemName
        val ivItem = view.ivCart
        val price = view.tvCartPrice
        val quantityValue = view.tvQuantityValue
        val cvPlus = view.cvPlus
        val cvMinus = view.cvMinus
        val quantityHeader = view.tvQuantity
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<ProductItems>() {
            override fun areItemsTheSame(oldItem: ProductItems, newItem: ProductItems): Boolean {
                return oldItem.quantity==newItem.quantity
            }
            override fun areContentsTheSame(oldItem: ProductItems, newItem: ProductItems): Boolean {
                return oldItem.quantity==newItem.quantity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
     return CartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_cart, parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        list[position].let { product ->
            holder.apply {
                tvName.text = product.id
                price.text = product.price
                Picasso.get().load(product.imageUrl).into(ivItem)
                if(key==Constants.FAVORITE_) {
                    quantityValue.makeGone()
                    cvPlus.makeGone()
                    cvMinus.makeGone()
                    quantityHeader.makeGone()
                }
                else {
                    quantityValue.makeVisible()
                    quantityValue.text = product.quantity.toString()
                    cvPlus.makeVisible()
                    cvMinus.makeVisible()
                    quantityHeader.makeVisible()
                    cvMinus.setOnClickListener{
                        listener?.onDecrementQuantity(product)
                    }
                    cvPlus.setOnClickListener {
                        listener?.onIncrementQuantity(product)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
}