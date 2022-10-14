package com.sutechshop.network

import com.sutechshop.model.Cart
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
    private val accessKey = ***REMOVED***

    fun getAllProducts() =
        databaseApi.getAllProducts(
            Select(accessKey, "products")
        )

    fun searchProducts(query: String) =
        databaseApi.getAllProducts(
            Select(accessKey, "products", "name LIKE '%$query%'")
        )

    fun updateProduct(productId: Int, quantity: Int): Call<String> {
        return databaseApi.updateProduct(
            Update(
                accessKey,
                "products",
                "quantity=$quantity",
                "id=$productId"
            )
        )
    }

    fun sendVerificationCode(number: String) =
        smsApi.sendVerificationCode(
            SendCode(number)
        )

    fun verifyCode(number: String, code: String): Call<Boolean> {
        return smsApi.verifyCode(
            VerifyCode(number, code)
        )
    }

    fun getUsers(number: String) =
        databaseApi.getUsers(
            Select(accessKey, "users", "number='$number'")
        )

    fun addUser(name: String, number: String, gender: Int): Call<String> {
        val values = "(name, number, gender) values ('$name', '$number', $gender)"
        return databaseApi.addUser(
            Insert(accessKey, "users", values)
        )
    }

    fun setUser(name: String, number: String, gender: Int): Call<String> {
        val set = "name='$name', gender=$gender"
        return databaseApi.setUser(
            Update(accessKey, "users", set, "number='$number'")
        )
    }

    fun deleteUser(number: String) =
        databaseApi.deleteUser(
            Delete(accessKey, "users", "number='$number'")
        )

    fun addToCart(number: String, productId: Int): Call<String> {
        val values = "(userId, productId, quantity) values ('$number', $productId, 1)"
        return databaseApi.addToCart(
            Insert(accessKey, "cart", values)
        )
    }

    fun removeFromCart(number: String, productId: Int): Call<String> {
        return databaseApi.removeFromCart(
            Delete(
                accessKey,
                "cart",
                "userId='$number' and productId=$productId"
            )
        )
    }

    fun updateCart(number: String, productId: Int, quantity: Int): Call<String> {
        return databaseApi.updateCart(
            Update(
                accessKey,
                "cart",
                "quantity=$quantity",
                "userId='$number' and productId=$productId"
            )
        )
    }

    fun getCartItem(number: String, productId: Int): Call<List<Cart>> {
        return databaseApi.getCartItem(
            Select(
                accessKey,
                "cart",
                "userId='$number' and productId=$productId"
            )
        )
    }

    fun getUserCart(number: String): Call<List<Cart>> {
        return databaseApi.getUserCart(
            Select(
                accessKey,
                "cart",
                "userId='$number'"
            )
        )
    }

    fun checkout(number: String): Call<String> {
        return databaseApi.checkout(
            Delete(
                accessKey,
                "cart",
                "userId='$number'"
            )
        )
    }

}