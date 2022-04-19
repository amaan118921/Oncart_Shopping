package com.example.oncart.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.oncart.R
import com.example.oncart.activities.ShoppingActivity
import com.example.oncart.helper.Constants
import kotlinx.android.synthetic.main.activity_shopping.*

abstract class BaseFragment: androidx.fragment.app.Fragment(), View.OnClickListener {
    abstract fun getLayout(): Int

    var finish = false
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

    fun addFragment(id: String, bundle: Bundle?) {
        (requireActivity() as ShoppingActivity).supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            add(R.id.fragmentShoppingContainer, Constants.getFragmentClass(id), bundle, id)
            addToBackStack(id)
        }
    }

     fun replaceFragment(id: String, bundle: Bundle?) {
        (requireActivity() as ShoppingActivity).supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            replace(R.id.fragmentShoppingContainer, Constants.getFragmentClass(id), bundle,id)
            addToBackStack(id)
        }
    }

    private fun getSupportFragmentManager(): FragmentManager {
        return (requireActivity() as ShoppingActivity).supportFragmentManager
    }

    fun removeFragment(frag: Fragment) {
        getSupportFragmentManager().commit {
            getSupportFragmentManager().findFragmentByTag(Constants.getIdByFragment(frag))
                ?.let { remove(it) }
        }
    }

    fun popBackStack() {
           getSupportFragmentManager().popBackStack()
    }

    fun removeOTP() {
        getSupportFragmentManager().commit {
            getSupportFragmentManager().findFragmentByTag(Constants.OTP_ID)
                ?.let { remove(it) }
        }
    }

    fun removeFragment(id: String) {
        getSupportFragmentManager().commit {
            getSupportFragmentManager().findFragmentByTag(id)
                ?.let { remove(it) }
        }
    }

    fun bottomGone() {
        (requireActivity() as ShoppingActivity).hideBottomNav()
    }

    fun bottomVisible() {
        (requireActivity() as ShoppingActivity).showBottomNav()
    }

    fun initViewPager() {
        (requireActivity() as ShoppingActivity).initViewPagerWithBottom()
    }

    fun showBottomSheet(id: String, bundle: Bundle?) {
        Constants.getBottomSheet(id).apply {
            arguments = bundle
            show(getSupportFragmentManager(), id)
        }
    }
}