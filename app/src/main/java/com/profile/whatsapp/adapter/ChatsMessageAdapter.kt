package com.profile.whatsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.profile.whatsapp.Interface.OnChatsMessageInterface
import com.profile.whatsapp.databinding.ItemRecieverLayoutBinding
import com.profile.whatsapp.databinding.ItemSenderLayoutBinding
import com.profile.whatsapp.model.MessageModel

class ChatsMessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var arrMessage: ArrayList<MessageModel> = ArrayList()
    private val SENDER_VIEW_TYPE = 1
    private val RECIEVER_VIEW_TYPE = 2
    var chatsMessageOnClick: OnChatsMessageInterface? = null

    class SenderViewHolder(val binding: ItemSenderLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class RecieverViewHolder(val binding: ItemRecieverLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENDER_VIEW_TYPE) {
            val view =
                ItemSenderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SenderViewHolder(view)
        } else {
            val view = ItemRecieverLayoutBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
            RecieverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SenderViewHolder::class.java) {
            (holder as SenderViewHolder).binding.tvSenderMessage.text = arrMessage[position].message
        } else {
            (holder as RecieverViewHolder).binding.tvRecieverMessage.text =
                arrMessage[position].message
        }
        holder.itemView.setOnLongClickListener{chatsMessageOnClick!!.onLongClicked(arrMessage[position])}
    }

    override fun getItemCount(): Int {
        return arrMessage.size
    }

    override fun getItemViewType(position: Int): Int {
        if (arrMessage[position].userId == FirebaseAuth.getInstance().uid) {
            return SENDER_VIEW_TYPE
        } else {
            return RECIEVER_VIEW_TYPE
        }
    }

    fun setData(list: ArrayList<MessageModel>) {
        arrMessage = list
        notifyDataSetChanged()
    }
}