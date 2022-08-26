package com.profile.whatsapp.model

class MessageModel {
    var userId: String? = null
    var message: String? = null
    var timeStamp: String? = null
    var messageId: String? = null

    constructor(userId: String, message: String) {
        this.userId = userId
        this.message = message
    }

    constructor() {}
}