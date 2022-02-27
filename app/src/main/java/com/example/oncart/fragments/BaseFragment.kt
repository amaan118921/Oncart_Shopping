package com.example.oncart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.oncart.R
import com.example.oncart.activities.MainActivity
import com.example.oncart.activities.ShoppingActivity
import com.example.oncart.helper.Constants
import com.example.oncart.helper.makeGone
import kotlinx.android.synthetic.main.activity_shopping.*

abstract class BaseFragment: Fragment(), View.OnClickListener {
    abstract fun getLayout(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    fun navigateTo(layoutNavId: Int, bundle: Bundle) {
        (requireActivity() as ShoppingActivity).getNavController()?.navigate(layoutNavId, bundle)
    }

    fun finish() {
        requireActivity().finish()
    }

    fun gotToHome() {
        (requireActivity() as ShoppingActivity).goToHome()
    }

    fun popBackStack() {
        (requireActivity() as ShoppingActivity).getNavController()?.popBackStack()
    }

    fun addFragment(id: String, bundle: Bundle) {
        (requireActivity() as ShoppingActivity).supportFragmentManager.commit {
            add(R.id.fragmentShoppingContainer, ProductDetailFragment::class.java, bundle, Constants.PRODUCT_DETAIL)
            setReorderingAllowed(true)
            addToBackStack(Constants.PRODUCT_DETAIL)
        }
    }

    private fun getSupportFragmentManager(): FragmentManager {
        return (requireActivity() as ShoppingActivity).supportFragmentManager
    }

    fun removeFragment() {
        getSupportFragmentManager().commit {
            getSupportFragmentManager().findFragmentByTag(Constants.PRODUCT_DETAIL)
                ?.let { remove(it) }
        }
    }

    fun bottomGone() {
        (requireActivity() as ShoppingActivity).hideBottomNav()
    }

    fun bottomVisible() {
        (requireActivity() as ShoppingActivity).showBottomNav()
    }

}