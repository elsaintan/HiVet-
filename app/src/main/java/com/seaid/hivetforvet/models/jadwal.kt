package com.seaid.hivetforvet.models

class jadwal {
    var id : String?=null
    var start : String?= null
    var end : String?= null
    var duration : String ?= null
    var slot : String?= null

    constructor(id: String?, start: String?, end: String?, duration: String?, slot: String?) {
        this.id = id
        this.start = start
        this.end = end
        this.duration = duration
        this.slot = slot
    }

    constructor()
}