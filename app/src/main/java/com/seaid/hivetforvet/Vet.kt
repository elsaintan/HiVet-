package com.seaid.hivetforvet

class Vet {
    var Name: String? = null
    var Contact: String? = null
    var uid: String? = null
    var photoProfile : String? = null
    var email : String? = null
    var SIP : String? = null
    var STR : String? = null
    var konsultasi : String?=null
    var booking : String?=null

    constructor()

    constructor(Name: String?, Contact: String?, uid: String?, photoProfile: String?, email: String?, SIP: String?, STR: String?, konsultasi: String?, booking: String?) {
        this.Name = Name
        this.Contact = Contact
        this.uid = uid
        this.photoProfile = photoProfile
        this.email = email
        this.SIP = SIP
        this.STR = STR
        this.konsultasi = konsultasi
        this.booking = booking
    }


}