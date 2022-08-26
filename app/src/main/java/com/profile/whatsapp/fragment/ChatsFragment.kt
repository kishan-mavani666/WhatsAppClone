package com.profile.whatsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.profile.whatsapp.R
import com.profile.whatsapp.adapter.ChatsListAdapter
import com.profile.whatsapp.databinding.FragmentChatBinding
import com.profile.whatsapp.model.UserModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatsAdapter: ChatsListAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)

        initBind()
        setData()
        initObserver()
        initListener()
        binding.executePendingBindings()
        return binding.root
    }

    private fun initBind() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        chatsAdapter = ChatsListAdapter()
        binding.rlChats.adapter = chatsAdapter
    }

    private fun setData() {
        database.reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatsList: ArrayList<UserModel> = ArrayList()
                for (snapValue in snapshot.children) {
                    val list = snapValue.getValue(UserModel::class.java)!!
                    list.userId=snapValue.key
                    if (list.userId!=auth.uid){chatsList.add(list)}
                }
                chatsAdapter.setData(chatsList)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun initObserver() {

    }

    private fun initListener() {

    }
}