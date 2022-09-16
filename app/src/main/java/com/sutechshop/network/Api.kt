package com.sutechshop.network

import com.sutechshop.model.Product
import com.sutechshop.model.query.Select
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("api/")
    fun getAllProducts(@Body select: Select): Call<List<Product>>


}