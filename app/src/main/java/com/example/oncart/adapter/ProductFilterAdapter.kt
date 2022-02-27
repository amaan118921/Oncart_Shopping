package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.model.StartingScreenModel
import kotlinx.android.synthetic.main.item_view_filter.view.*

class ProductFilterAdapter(private val context: Context, private val listener: IListener): RecyclerView.Adapter<ProductFilterAdapter.ProductFilterViewHolder>() {

    private var prevIndex = 0
    private var currentPos = 0
    interface IListener {
        fun onFilterItemClick(title: String)
    }

    private var list = arrayListOf<StartingScreenModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun bindView(list: ArrayList<StartingScreenModel>) {
        this.list = list
        notifyDataSetChanged()
    }
    private var size = 5
    inner class ProductFilterViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var imgView = view.ivProduct
        var tv = view.tvProductName
        var filterCard = view.filterCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductFilterViewHolder {
        return ProductFilterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_filter, parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ProductFilterViewHolder, position: Int) {
       list[position].let { model ->
           holder.apply {
               if(prevIndex==currentPos) {filterCard.cardElevation = 10F} else {filterCard.cardElevation = 0F}
               imgView.setImageDrawable(context.getDrawable(model.imgId))
               tv.text = model.title
               filterCard.setOnClickListener {
                   listener.onFilterItemClick(model.title)
                   prevIndex = currentPos
                   currentPos = position
                   notifyItemChanged(prevIndex)
               }
           }
       }
    }

    override fun getItemCount(): Int {
        return size
    }
}