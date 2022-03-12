package com.seaid.hivetforvet.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KonsultasiViewModel(kstatus : String) : ViewModel() {

    var status = MutableLiveData<String>()

    init {
        status.value = ""
    }

    fun setValue(status: String){
        this.status.value = status
    }
}