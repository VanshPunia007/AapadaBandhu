package com.priyavansh.aapadabandhu.models

class Report {
    var type: String? = null
    var location: String? = null
    var description: String? = null
    var image: String? = null
    var userID: String? = null
    var userName: String? = null
    var userEmail: String? = null
    var lat: Float? = null
    var long: Float? = null
    var time: String? = null

    constructor()

    constructor(type: String?, location: String?, description: String?, image: String?, userName: String?,
                userEmail: String?,lat: Float?, long: Float?, time: String?, userID: String?){
        this.type = type
        this.location = location
        this.description = description
        this.image = image
        this.userName = userName
        this.userEmail = userEmail
        this.lat = lat
        this.long = long
        this.time = time
        this.userID = userID
    }
}