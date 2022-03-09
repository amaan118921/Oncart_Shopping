package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.model.ProductItems
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_products.view.*

class SearchAdapter(private val listener: Listener): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(){

    interface Listener {
        fun onProductItemClick(productItem: ProductItems)
    }
    var arrayList = arrayListOf<ProductItems>()

    @SuppressLint("NotifyDataSetChanged")
    fun bindList(arrayList: ArrayList<ProductItems>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }
    class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_products, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        arrayList[position].let { product ->
            holder.itemView.apply {
                tvItemName.text = product.id
                tvPrice.text = product.price
                Picasso.get().load(product.imageUrl).into(ivItem)
                setOnClickListener{
                    listener.onProductItemClick(product)
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return arrayList.size
    }
}