package com.sutechshop.network

import com.sutechshop.model.Product
import com.sutechshop.model.query.Select
import retrofit2.Call

class Repository {

    private val api = RetrofitService().build()
    private val accessKey = "GPCZPw7s11kgyPTxREJH2FnWbCyQM4fI"

    fun getAllProducts(select: String = "products"): Call<List<Product>> =
        api.getAllProducts(Select(accessKey, select))

}