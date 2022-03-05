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

    private var currentIndex = 0
    private var prevIndex = 0
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
               if(currentIndex==position) {
                   filterCard.cardElevation = 10F
                   filterCard.strokeColor = context.resources.getColor(R.color.app_blue)
               }else {
                   filterCard.cardElevation = 0F
                   filterCard.strokeColor = context.resources.getColor(R.color.login_card_stroke)
               }
               imgView.setImageDrawable(context.getDrawable(model.imgId))
               tv.text = model.title
               filterCard.setOnClickListener {
                   listener.onFilterItemClick(model.title)
                   filterCard.cardElevation = 10F
                   filterCard.strokeColor = context.resources.getColor(R.color.app_blue)
                   prevIndex = currentIndex
                   currentIndex = position
                   notifyItemChanged(prevIndex)
               }
           }
       }
    }

    override fun getItemCount(): Int {
        return size
    }
}