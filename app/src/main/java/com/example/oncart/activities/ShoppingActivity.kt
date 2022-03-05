package com.example.oncart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.example.oncart.R
import com.example.oncart.adapter.ViewPagerAdapter
import com.example.oncart.eventBus.MessageEvent
import com.example.oncart.fragments.LoginFragment
import com.example.oncart.fragments.StartingFragment
import com.example.oncart.helper.Constants
import com.example.oncart.helper.HelpRepo
import com.example.oncart.helper.makeGone
import com.example.oncart.helper.makeVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_shopping.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private var navHostFragment: NavHostFragment? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    @Inject
    lateinit var repo: HelpRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        setContentView(R.layout.activity_shopping)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentShoppingContainer) as NavHostFragment
        if(repo.getSharedPreferences(Constants.PHONE)=="") {
            addFragment()
        }else {
            initViewPagerWithBottom()
        }
    }

    fun initViewPagerWithBottom() {
        initViewPager()
        setUpBottomNavigation()
    }

    private fun addFragment() {
        supportFragmentManager.commit {
            add(R.id.fragmentShoppingContainer, StartingFragment(), Constants.STARTING_SCREEN_FRAGMENT_ID)
            setReorderingAllowed(true)
            addToBackStack(Constants.STARTING_SCREEN_FRAGMENT_ID)
        }
    }
    fun getNavController(): NavController? {
        return navHostFragment?.navController
    }

    @Subscribe
    fun onMessageEvent(event: MessageEvent) {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun setUpBottomNavigation() {
        btmNavigation.setOnNavigationItemSelectedListener(this)
    }

    private fun initViewPager() {
        viewPagerAdapter = navHostFragment?.fragmentManager?.let {
            ViewPagerAdapter(it)
        }
        viewPager?.adapter = viewPagerAdapter
        viewPager.addOnPageChangeListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.homeBottom -> {viewPager.setCurrentItem(0, true)}
            R.id.accountBottom -> {viewPager.setCurrentItem(3, true)}
            R.id.searchBottom -> {viewPager.setCurrentItem(1, true)}
            R.id.LikedBottom -> {viewPager.setCurrentItem(2, true)}
        }
        return false
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        changeBottomTabByPosition(position)
    }

    private fun changeBottomTabByPosition(position: Int) {
        when(position) {
            0 -> {
                btmNavigation.menu.getItem(0).isChecked = true
            }
            1-> {
                btmNavigation.menu.getItem(1).isChecked = true
            }
            2 -> {
                btmNavigation.menu.getItem(2).isChecked = true
            }
            3 -> {
                btmNavigation.menu.getItem(3).isChecked = true
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun goToHome() {
        viewPager.setCurrentItem(0, true)
    }

    fun hideBottomNav() {
        btmNavigation.makeGone()
    }

    fun showBottomNav() {
        btmNavigation.makeVisible()
    }
}