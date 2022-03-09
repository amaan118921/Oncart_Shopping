package com.example.oncart.fragments

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oncart.R
import com.example.oncart.Utils.Utils
import com.example.oncart.adapter.ProfileAdapter
import com.example.oncart.eventBus.MessageEvent
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.example.oncart.helper.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_dialog.*
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDialog: BaseFragment(), ProfileAdapter.Listener {

    override fun getLayout(): Int {
        return R.layout.profile_dialog
    }

    @Inject
    lateinit var repo: HelpRepo

    private var editMode = false
    private lateinit var adapter: ProfileAdapter
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editMode = arguments?.getBoolean(Constants.EDIT_MODE)?:false
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        (requireActivity()).onBackPressedDispatcher.addCallback(this) {
            if(editMode){popBackStack()}
        }
        btnSave.setOnClickListener(this)
        adapter = ProfileAdapter(requireContext(), this)
        rvProfile?.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@ProfileDialog.adapter
        }
        ivClose.setOnClickListener(this)
        if(editMode) {
            ivClose.makeVisible()
            etName.setText(repo.getSharedPreferences(Constants.NAME))
            this.id = repo.getSharedPreferences(Constants.AVATAR_ID).toIntOrNull()?:0
            adapter.bindView(getList())
            adapter.checked = getList().indexOf(id)
            adapter.notifyItemChanged(getList().indexOf(id))
        }else  adapter.bindView(getList())
    }
    private fun getList(): ArrayList<Int> {
        return arrayListOf(R.drawable.man, R.drawable.man__1_, R.drawable.woman, R.drawable.woman__1_, R.drawable.avatar
            , R.drawable.gamer)
    }
    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnSave -> {validate()}
            R.id.ivClose -> {popBackStack()}
        }
    }
    private fun validate() {
        val name = etName.text.toString().trim()
        if(name.isEmpty()) {Utils.showToast(requireActivity(), "Enter your name")}
        else if(id==null){Utils.showToast(requireActivity(), "Choose an avatar")}
        else if(name.isEmpty() && id==null) {Utils.showToast(requireActivity(), "Choose avatar and Enter your name")}
        else {
            repo.apply {
                setSharedPreferences(Constants.NAME, name)
                id?.toString()?.let {setSharedPreferences(Constants.AVATAR_ID, it)}
                setSharedPreferences(Constants.LOGGED_IN, Constants.LOGGED_IN)
            }
            if(editMode) {EventBus.getDefault().post(MessageEvent(getString(R.string.update_profile))) }
             popBackStack()
        }
    }
    override fun onAvatarClick(id: Int) {
        this.id = id
    }
}