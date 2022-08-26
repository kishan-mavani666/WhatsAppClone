package com.profile.whatsapp.model

class UserModel {
    var password: String? = null
    var userName: String? = null
    var email: String? = null
    var userId: String? = null
    var userProfile: String? = null
    var lastMessage: String? = null
    var about:String?=null

    constructor() {}

    /* SignUp */
    constructor(userName: String?, email: String?, password: String?) {
        this.password = password
        this.userName = userName
        this.email = email
    }
}