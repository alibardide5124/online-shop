package com.sutechshop.network

import com.sutechshop.model.Product
import com.sutechshop.model.query.Delete
import com.sutechshop.model.query.Insert
import com.sutechshop.model.query.Select
import com.sutechshop.model.query.Update
import com.sutechshop.model.sms.SendCode
import com.sutechshop.model.sms.VerifyCode
import retrofit2.Call

class Repository {

    private val databaseApi = RetrofitService().buildDatabaseService()
    private val smsApi = RetrofitService().buildSmsService()
    private val accessKey = "F6tIj8U0Lsb42hw0xPhIYVWSR58cXDXh"

    fun getAllProducts() =
        databaseApi.getAllProducts(Select(accessKey, "products"))

    fun searchProducts(query: String) =
        databaseApi.getAllProducts(Select(accessKey, "products", "name=$query"))

    fun sendVerificationCode(number: String) =
        smsApi.sendVerificationCode(SendCode(number))

    fun verifyCode(number: String, code: String) =
        smsApi.verifyCode(VerifyCode(number, code))

    fun getUsers(number: String) =
        databaseApi.getUsers(Select(accessKey, "users", "number=$number"))

    fun addUser(name: String, number: String, gender: Int): Call<String> {
        val values = "(name, number, gender) values ('$name', '$number', $gender)"
        return databaseApi.addUser(Insert(accessKey, "users", values))
    }

    fun setUser(name: String, number: String, gender: Int): Call<String> {
        val set = "name='$name', gender=$gender"
        return databaseApi.setUser(Update(accessKey, "users", set, "number='$number'"))
    }

    fun deleteUser(number: String) =
        databaseApi.deleteUser(Delete(accessKey, "users", "number='$number'"))
}