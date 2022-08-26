package com.profile.whatsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.profile.whatsapp.adapter.FragmentAdapter
import com.profile.whatsapp.databinding.ActivityMainBinding
import com.profile.whatsapp.fragment.CallsFragment
import com.profile.whatsapp.fragment.ChatFragment
import com.profile.whatsapp.fragment.StatusFragment
import com.profile.whatsapp.ui.authentication.LoginActivity
import com.profile.whatsapp.ui.base.BaseActivity
import com.profile.whatsapp.ui.menu.GroupChatsActivity
import com.profile.whatsapp.ui.menu.SettingsActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentAdapter: FragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBind()
        setData()
        initObserver()
        initListener()
        binding.executePendingBindings()
    }

    private fun initBind() {
        auth = FirebaseAuth.getInstance()
        fragmentAdapter = FragmentAdapter(supportFragmentManager)
    }

    private fun setData() {
        fragmentAdapter.addFragment(ChatFragment(), "CHATS")
        fragmentAdapter.addFragment(StatusFragment(), "STATUS")
        fragmentAdapter.addFragment(CallsFragment(), "CALL")

        binding.mainViewpager.adapter = fragmentAdapter
        binding.mainTablayout.setupWithViewPager(binding.mainViewpager)

    }

    private fun initObserver() {

    }

    private fun initListener() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> openActivity(SettingsActivity::class.java)
            R.id.group_chat -> openActivity(GroupChatsActivity::class.java)
            R.id.logout -> {
                auth.signOut()
                openActivity(LoginActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)

    }
}