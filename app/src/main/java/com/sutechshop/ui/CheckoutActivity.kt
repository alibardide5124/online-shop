package com.sutechshop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sutechshop.databinding.ActivityCheckoutBinding
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val checkoutCall = repository.checkout(user.number)
        checkoutCall.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.contains("done")) {
                        Toast.makeText(applicationContext, "پرداخت شد", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@CheckoutActivity, "عملیات موفقیت امیز نبود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CheckoutActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUser(): User {
        val preferences = getSharedPreferences("sutech", MODE_PRIVATE)
        val userData = preferences.getString("user", "")
        return Gson().fromJson(userData, User::class.java)
    }

}