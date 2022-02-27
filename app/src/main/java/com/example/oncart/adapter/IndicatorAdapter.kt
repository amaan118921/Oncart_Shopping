package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import kotlinx.android.synthetic.main.item_indicator.view.*

class IndicatorAdapter(private val context: Context): RecyclerView.Adapter<IndicatorAdapter.IndicatorViewHolder>() {

    private var listSize = 3
    var currentPos = 0
    inner class IndicatorViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val dot = view.dot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicatorViewHolder {
        return IndicatorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_indicator, parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: IndicatorViewHolder, position: Int) {
       if(currentPos==position) {holder.dot.background = context.getDrawable(R.drawable.selected_dot)}
        else holder.dot.background = context.getDrawable(R.drawable.default_dot)
    }

    override fun getItemCount(): Int {
        return listSize
    }
}