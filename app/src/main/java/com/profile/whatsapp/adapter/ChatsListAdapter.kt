package com.profile.whatsapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.profile.whatsapp.R
import com.profile.whatsapp.databinding.ItemChatLayoutBinding
import com.profile.whatsapp.model.UserModel
import com.profile.whatsapp.ui.chat.ChatsDetailsActivity

class ChatsListAdapter : RecyclerView.Adapter<ChatsListAdapter.ViewHolder>() {

    private var arrChatsList: ArrayList<UserModel> = ArrayList()

    class ViewHolder(val binding: ItemChatLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemChatLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        Glide
            .with(context)
            .load(arrChatsList[position].userProfile)
            .centerCrop()
            .placeholder(R.drawable.img_user_profile)
            .into(holder.binding.profileImage)
        holder.binding.tvUserName.text = arrChatsList[position].userName

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatsDetailsActivity::class.java)
            intent.putExtra("USER_ID", arrChatsList[position].userId)
            intent.putExtra("USER_NAME", arrChatsList[position].userName)
            intent.putExtra("USER_PROFILE", arrChatsList[position].userProfile)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return arrChatsList.size
    }

    fun setData(list: ArrayList<UserModel>) {
        arrChatsList = list
        notifyDataSetChanged()
    }
}