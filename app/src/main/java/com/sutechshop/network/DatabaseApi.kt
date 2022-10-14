package com.sutechshop.network

import com.sutechshop.model.Cart
import com.sutechshop.model.Product
import com.sutechshop.model.User
import com.sutechshop.model.query.Delete
import com.sutechshop.model.query.Insert
import com.sutechshop.model.query.Select
import com.sutechshop.model.query.Update
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DatabaseApi {

    @POST("api/")
    fun getAllProducts(@Body select: Select): Call<List<Product>>

    @POST("api/")
    fun updateProduct(@Body update: Update): Call<String>

    @POST("api/")
    fun getUsers(@Body select: Select): Call<List<User>>

    @POST("api/")
    fun addUser(@Body insert: Insert): Call<String>

    @POST("api/")
    fun setUser(@Body update: Update): Call<String>

    @POST("api/")
    fun deleteUser(@Body delete: Delete): Call<String>

    @POST("api/")
    fun addToCart(@Body insert: Insert): Call<String>

    @POST("api/")
    fun removeFromCart(@Body delete: Delete): Call<String>

    @POST("api/")
    fun updateCart(@Body update: Update): Call<String>

    @POST("api/")
    fun getCartItem(@Body select: Select): Call<List<Cart>>

    @POST("api/")
    fun getUserCart(@Body select: Select): Call<List<Cart>>

    @POST("api/")
    fun checkout(@Body delete: Delete): Call<String>

}