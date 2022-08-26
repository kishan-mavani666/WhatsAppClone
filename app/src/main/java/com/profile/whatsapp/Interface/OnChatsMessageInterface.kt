package com.profile.whatsapp.Interface

import com.profile.whatsapp.model.MessageModel

interface OnChatsMessageInterface {

    fun onLongClicked(messageModel: MessageModel): Boolean
}