package com.robert.lookout.ui.main.util

sealed class Resources<T>(
    val data:T?=null,
    val message:String?=null
) {
    class Success<T>(data: T?):Resources<T>(data)
    class Loading<T>():Resources<T>()
    class Error<T>(msg: String?,data: T?=null):Resources<T>(data,msg)

}