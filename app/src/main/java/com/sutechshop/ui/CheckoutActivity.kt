package com.sutechshop.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sutechshop.databinding.ActivityCheckoutBinding
import com.sutechshop.model.Cart
import com.sutechshop.model.Product
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var products: List<Product>
    private lateinit var cartList: List<Cart>
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productsJson = intent.extras!!.getString("products")
        val productListType = object: TypeToken<List<Product>>(){}.type
        products = Gson().fromJson(productsJson, productListType)

        val cartListJson = intent.extras!!.getString("cartList")
        val cartListType = object: TypeToken<List<Cart>>(){}.type
        cartList = Gson().fromJson(cartListJson, cartListType)

        setupViews()
    }

    private fun setupViews() {
        binding.checkoutButtonCode2.setOnClickListener {
            binding.checkoutButtonCode2.text = "کلیک شد"
        }
        binding.checkoutBtnPay.setOnClickListener {
            checkout(getUser())
        }
    }

    private fun checkout(user: User) {
        showLoading()

        val checkoutCall = repository.checkout(user.number)
        checkoutCall.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.contains("done")) {

                        CoroutineScope(Dispatchers.IO).launch {
                            lowerQuantity()
                        }

                        Toast.makeText(applicationContext, "پرداخت شد", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@CheckoutActivity, "عملیات موفقیت امیز نبود", Toast.LENGTH_SHORT).show()
                        showCard()
                    }
                } else {
                    Toast.makeText(this@CheckoutActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                    showCard()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                showCard()
            }
        })
    }

    private fun lowerQuantity() {
        for (item in cartList) {
            val product = products.filter { it.id == item.productId }[0]
            val updateProductCall = repository.updateProduct(product.id, product.quantity - item.quantity)
            updateProductCall.execute()
        }
    }

    private fun getUser(): User {
        val preferences = getSharedPreferences("sutech", MODE_PRIVATE)
        val userData = preferences.getString("user", "")
        return Gson().fromJson(userData, User::class.java)
    }

    private fun showLoading() {
        binding.checkoutCardLayout.visibility = View.GONE
        binding.checkoutLoading.visibility = View.VISIBLE
    }

    private fun showCard() {
        binding.checkoutCardLayout.visibility = View.VISIBLE
        binding.checkoutLoading.visibility = View.GONE
    }

}