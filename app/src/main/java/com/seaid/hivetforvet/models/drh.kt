package com.seaid.hivetforvet.models

class drh {
    var id : String?= null
    var Name : String ?= null
    var WorkExp : String ?= null
    var harga : String ?= null
    var booking : String ?= null
    var tempat : String ?= null
    var alamat : String ?= null
    var photoProfile : String ?= null

    constructor()

    constructor(
        id: String?,
        Name: String?,
        WorkExp: String?,
        harga: String?,
        booking: String?,
        tempat: String?,
        alamat: String?,
        photoProfile: String?,
    ) {
        this.id = id
        this.Name = Name
        this.WorkExp = WorkExp
        this.harga = harga
        this.booking = booking
        this.tempat = tempat
        this.alamat = alamat
        this.photoProfile = photoProfile
    }


}