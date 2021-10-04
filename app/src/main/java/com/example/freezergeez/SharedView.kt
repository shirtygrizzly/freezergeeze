package com.example.freezergeez

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedView:ViewModel() {

    private var _itemName =MutableLiveData<String>()
    val itemName :LiveData<String> = _itemName
    private var _itemDesc =MutableLiveData<String>()
    val itemDesc :LiveData<String> = _itemDesc
    private var _itemQty =MutableLiveData<String>()
    val itemQty :LiveData<String> = _itemQty
    private var _itemUrl =MutableLiveData<String>()
    val itemUrl :LiveData<String> = _itemUrl

    fun saveItem( itemName:String?, itemDesc :String?,itemQty:String?,itemUrl:String? ) {

        _itemName.value = itemName!!
        _itemDesc.value = itemDesc!!
        _itemQty.value = itemQty!!
        _itemUrl.value = itemUrl!!


    }

}