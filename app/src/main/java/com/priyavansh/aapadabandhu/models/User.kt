package com.priyavansh.aapadabandhu.models

class User {
    var name: String? = null
    var email: String? = null
    var aadhar: String?= null
    var password: String? = null
    var image: String? = null

    constructor()

    constructor(name: String?, email: String?, aadhar: String?, password: String?, image: String?) {
        this.name = name
        this.email = email
        this.aadhar = aadhar
        this.password = password
        this.image = image
    }

    constructor(name: String?, email: String?, aadhar: String?, password: String?) {
        this.name = name
        this.email = email
        this.aadhar = aadhar
        this.password = password
    }

}