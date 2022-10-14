package com.sutechshop.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sutechshop.adapter.CartListAdapter
import com.sutechshop.databinding.ActivityCartBinding
import com.sutechshop.model.Cart
import com.sutechshop.model.Product
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var preferences: SharedPreferences
    private val repository = Repository()
    private lateinit var products: List<Product>
    private lateinit var cartList: List<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("sutech", MODE_PRIVATE)

        setupViews()
    }

    private fun setupViews() {
        binding.cartBack.setOnClickListener { finish() }

        setupCartList()
        binding.cartBtnPay.setOnClickListener {
            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
            intent.putExtra("products", Gson().toJson(products))
            intent.putExtra("cartList", Gson().toJson(cartList))
            startActivity(intent)
        }
        binding.cartBtnRetry.setOnClickListener {
            setupCartList()
        }
    }

    private fun setupCartList() {
        showLoading()
        val getAllProductsCall = repository.getAllProducts()
        getAllProductsCall.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    products = response.body()!!
                    setupRecyclerView(response.body()!!)
                }else
                    showError()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                showError()
            }
        })
    }

    private fun setupRecyclerView(products: List<Product>) {
        val userData = preferences.getString("user", "")
        val user = Gson().fromJson(userData, User::class.java)

        val getUserCartCall = repository.getUserCart(user.number)
        getUserCartCall.enqueue(object : Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                if (response.isSuccessful && response.body() != null) {
                    cartList = response.body()!!

                    val adapter = CartListAdapter(this@CartActivity, products, cartList)
                    binding.cartRv.layoutManager = LinearLayoutManager(this@CartActivity)
                    binding.cartRv.adapter = adapter

                    showCart()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                t.printStackTrace()
                showError()
            }
        })
    }

    private fun showLoading() {
        binding.cartListLayout.visibility = View.GONE
        binding.cartLoading.visibility = View.VISIBLE
        binding.cartRetry.visibility = View.GONE
    }

    private fun showError() {
        binding.cartListLayout.visibility = View.GONE
        binding.cartLoading.visibility = View.GONE
        binding.cartRetry.visibility = View.VISIBLE
    }

    private fun showCart() {
        binding.cartListLayout.visibility = View.VISIBLE
        binding.cartLoading.visibility = View.GONE
        binding.cartRetry.visibility = View.GONE
    }

}