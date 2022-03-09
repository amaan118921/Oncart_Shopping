package com.example.oncart.Utils

import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oncart.R
import com.example.oncart.adapter.ProfileAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Dialog(private val userContext: Context, private val list: ArrayList<Int>){

    private lateinit var dialogBuilder: AlertDialog
    private lateinit var adapter:ProfileAdapter

    fun initDialog() {
        dialogBuilder = MaterialAlertDialogBuilder(userContext).setTitle(userContext.getString(R.string.complete_profile))
            .setView(R.layout.profile_dialog).setCancelable(false).
            create()
        val rv = dialogBuilder.findViewById<RecyclerView>(R.id.rvProfile)
        rv?.apply {
            layoutManager = LinearLayoutManager(userContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@Dialog.adapter
        }
        dialogBuilder.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        adapter.bindView(list)
        dialogBuilder.show()
    }
}