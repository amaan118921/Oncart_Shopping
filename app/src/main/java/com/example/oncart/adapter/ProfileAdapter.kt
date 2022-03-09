package com.example.oncart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view_profile.view.*

class ProfileAdapter(private val context: Context, private val listener: Listener): RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    interface Listener {
        fun onAvatarClick(id: Int)
    }
    private var list = arrayListOf<Int>()
    var checked = -1
    @SuppressLint("NotifyDataSetChanged")
    fun bindView(list: ArrayList<Int>) {
        this.list = list
        notifyDataSetChanged()
    }
    class ProfileViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val iv = view.ivProfile
        val ivChecked = view.ivChecked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_profile, parent, false))
    }

    @SuppressLint("UseCompatLoadingForDrawables", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        list[position].let {
            holder.apply {
                if(checked!=-1 && checked==position) { ivChecked.makeVisible() }else ivChecked.makeGone()
                iv.setImageDrawable(context.getDrawable(it))
                iv.setOnClickListener {
                    checked = position
                    notifyDataSetChanged()
                    listener.onAvatarClick(list[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}