package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.model.StartingScreenModel
import kotlinx.android.synthetic.main.item_starting_screen.view.*

class StartingScreenAdapter(private val context: Context): RecyclerView.Adapter<StartingScreenAdapter.StartingScreenViewHolder>() {
    private var list = arrayListOf<StartingScreenModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun bindView(list: ArrayList<StartingScreenModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class StartingScreenViewHolder(view: View): RecyclerView.ViewHolder(view) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindView() {
            list[layoutPosition].apply {
                itemView.tvTitle.text = this.title
                if(layoutPosition==2) {itemView.ivStarting.layoutParams.height = 600
                    itemView.ivStarting.layoutParams.width = 600
                }
                itemView.ivStarting.setImageDrawable(context.getDrawable(this.imgId))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartingScreenViewHolder {
        return StartingScreenViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_starting_screen, parent, false))
    }

    override fun onBindViewHolder(holder: StartingScreenViewHolder, position: Int) {
        holder.bindView()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}