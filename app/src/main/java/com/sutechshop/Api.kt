package com.sutechshop

import retrofit2.http.POST

interface Api {

    @POST
    fun getAllProducts()

    @POST
    fun getProduct()

    @POST
    fun addToCart()

    @POST
    fun searchProducts()

    @POST
    fun payCheckout()

    @POST
    fun sendVerifyCode()

    @POST
    fun getUser()

    @POST
    fun setUser()

    @POST
    fun deleteUser()


}