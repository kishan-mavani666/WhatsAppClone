package com.profile.whatsapp.ui.menu

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.profile.whatsapp.R
import com.profile.whatsapp.adapter.ChatsMessageAdapter
import com.profile.whatsapp.databinding.ActivityGroupChatsBinding
import com.profile.whatsapp.model.MessageModel
import com.profile.whatsapp.ui.base.BaseActivity
import java.util.*

class GroupChatsActivity : BaseActivity() {
    private lateinit var binding: ActivityGroupChatsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
    private var senderUserId: String? = null
    private var arrChatsMessageList: ArrayList<MessageModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_chats)

        initBind()
        setData()
        initListener()
        initObserver()
        binding.executePendingBindings()
    }

    private fun initBind() {
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        chatsMessageAdapter = ChatsMessageAdapter()
        binding.rvChatsMessage.adapter = chatsMessageAdapter
    }

    private fun setData() {
        senderUserId = auth.uid

    }

    private fun initListener() {

        database.reference.child("Group Chats").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrChatsMessageList.clear()
                for (snapValue in snapshot.children) {
                    val messageModel = snapValue.getValue(MessageModel::class.java)
                    arrChatsMessageList.add(messageModel!!)
                }
                chatsMessageAdapter.setData(arrChatsMessageList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun initObserver() {
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivSend.setOnClickListener {
            val message = binding.etSendMessage.text.toString()
            if (!message.isNullOrEmpty()) {
                val messageModel = MessageModel(senderUserId.toString(), message)
                messageModel.timeStamp = Date().time.toString()
                binding.etSendMessage.setText("")

                database.reference.child("Group Chats")
                    .push()
                    .setValue(messageModel).addOnSuccessListener { onSuccess ->
                    }
                    .addOnFailureListener { onFailure ->
                        showToast(onFailure.message.toString())
                    }

            } else {
                showToast(getString(R.string.please_enter_message))
            }
        }

    }
}