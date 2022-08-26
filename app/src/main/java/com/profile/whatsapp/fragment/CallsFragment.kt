package com.profile.whatsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.profile.whatsapp.R
import com.profile.whatsapp.databinding.FragmentCallsBinding

class CallsFragment : Fragment() {

    private lateinit var binding: FragmentCallsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calls, container, false)

        initBind()
        setData()
        initObserver()
        initListener()
        binding.executePendingBindings()
        return binding.root
    }

    private fun initBind() {

    }

    private fun setData() {

    }

    private fun initObserver() {

    }

    private fun initListener() {

    }

}