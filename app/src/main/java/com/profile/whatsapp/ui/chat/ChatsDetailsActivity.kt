package com.profile.whatsapp.ui.chat

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.profile.whatsapp.Interface.OnChatsMessageInterface
import com.profile.whatsapp.R
import com.profile.whatsapp.adapter.ChatsMessageAdapter
import com.profile.whatsapp.databinding.ActivityChatsDetailsBinding
import com.profile.whatsapp.model.MessageModel
import com.profile.whatsapp.ui.base.BaseActivity
import java.util.*

class ChatsDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityChatsDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
    private var senderRoom: String? = null
    private var recieverRoom: String? = null
    private var recieverUserID: String? = null
    private var senderUserId: String? = null
    private var arrChatsMessageList: ArrayList<MessageModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats_details)

        initBind()
        setData()
        initObserver()
        initListener()
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
        recieverUserID = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")
        val userProfile = intent.getStringExtra("USER_PROFILE")
        binding.tvUserName.text = userName
        Glide
            .with(this)
            .load(userProfile)
            .centerCrop()
            .placeholder(R.drawable.img_user_profile)
            .into(binding.profileImage)
        senderRoom = senderUserId + recieverUserID
        recieverRoom = recieverUserID + senderUserId

        database.reference.child("Chats")
            .child(senderRoom.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrChatsMessageList.clear()
                    for (snapValue in snapshot.children) {
                        val messageModel = snapValue.getValue(MessageModel::class.java)
                        messageModel?.messageId=snapValue.key
                        arrChatsMessageList.add(messageModel!!)
                    }
                    chatsMessageAdapter.setData(arrChatsMessageList)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    private fun initObserver() {
        chatsMessageAdapter.chatsMessageOnClick=object : OnChatsMessageInterface{
            override fun onLongClicked(messageModel: MessageModel): Boolean {
                val alertDialog=AlertDialog.Builder(this@ChatsDetailsActivity)
                alertDialog.setTitle("Delete")
                alertDialog.setMessage("Are you sure you want to delete this message")
                alertDialog.setPositiveButton("Yes") { dialog: DialogInterface?,Int ->
                    database.reference.child("Chats").child(senderRoom.toString()).child(messageModel.messageId.toString())
                        .setValue(null)
                    dialog?.dismiss()
                }
                alertDialog.setNegativeButton("No") { dialog: DialogInterface?, Int ->
                    dialog?.dismiss()
                }
                alertDialog.show()
                return false
            }

        }
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivSend.setOnClickListener {
            val message = binding.etSendMessage.text.toString()
            if (!message.isNullOrEmpty()) {
                val messageModel = MessageModel(senderUserId.toString(), message)
                messageModel.timeStamp = Date().time.toString()
                binding.etSendMessage.setText("")

                database.reference.child("Chats")
                    .child(senderRoom.toString())
                    .push()
                    .setValue(messageModel).addOnSuccessListener { onSuccess ->
                        database.reference.child("Chats")
                            .child(recieverRoom.toString())
                            .push()
                            .setValue(messageModel).addOnSuccessListener { onSuccess ->

                            }
                            .addOnFailureListener { onFailure ->
                                showToast(onFailure.message.toString())
                            }
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