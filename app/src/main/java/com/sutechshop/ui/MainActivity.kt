package com.sutechshop.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sutechshop.R
import com.sutechshop.adapter.ProductListAdapter
import com.sutechshop.databinding.ActivityMainBinding
import com.sutechshop.model.Product
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.mainBtnMenu.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }
        binding.mainBtnRetry.setOnClickListener {
            setupRecyclerView()
        }
        setHeaderText()
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navPanel -> {
                    if (isUserLoggedIn()) {
                        val intent = Intent(this, UserPanelActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.navCart -> {
                    if (isUserLoggedIn()) {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.navLogout -> logout()
            }
            binding.mainDrawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
        setupRecyclerView()
    }

    private fun setHeaderText() {
        if (isUserLoggedIn()) {
            val userData = preferences.getString("user", "")
            val user = Gson().fromJson(userData, User::class.java)

            val headerView = binding.navView.getHeaderView(0)
            val textView: TextView = headerView.findViewById(R.id.drawerName)
            textView.text = user.name
        }
    }

    private fun setupRecyclerView() {
        showLoading()

        val getAllProductsCall = repository.getAllProducts()
        getAllProductsCall.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                Log.d("response", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    val adapter = ProductListAdapter(this@MainActivity, response.body()!!)
                    binding.mainRV.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.mainRV.adapter = adapter

                    if (response.body()!!.isNotEmpty())
                        showRecyclerView()
                    else
                        showEmpty()
                } else {
                    Log.e("Response", response.message())
                    showError()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                showError()
            }
        })
    }

    private fun isUserLoggedIn(): Boolean {
        preferences = getSharedPreferences("sutech", MODE_PRIVATE)
        val userData = preferences.getString("user", "")
        return userData != ""
    }

    private fun logout() {
        if (isUserLoggedIn()) {
            preferences.edit().putString("user", "").apply()
            Toast.makeText(applicationContext, "با موفقیت از حساب خارج شدید", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            AlertDialog.Builder(this)
                .setTitle("خروج از حساب کاربری")
                .setMessage("شما هنوز وارد حساب نشده اید!")
                .setPositiveButton("باشه", null)
                .create()
                .show()
        }
    }

    private fun showRecyclerView() {
        binding.mainRV.visibility = View.VISIBLE
        binding.mainLoading.visibility = View.GONE
        binding.mainRetry.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainRV.visibility = View.GONE
        binding.mainLoading.visibility = View.VISIBLE
        binding.mainRetry.visibility = View.GONE
    }

    private fun showError() {
        binding.mainRV.visibility = View.GONE
        binding.mainLoading.visibility = View.GONE
        binding.mainRetry.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.mainRV.visibility = View.GONE
        binding.mainLoading.visibility = View.GONE
        binding.mainRetry.visibility = View.GONE
        binding.mainTxtEmpty.visibility = View.VISIBLE
    }
}