package com.sutechshop.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.gson.Gson
import com.sutechshop.R
import com.sutechshop.databinding.ActivityDetailBinding
import com.sutechshop.model.Cart
import com.sutechshop.model.Product
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var product: Product
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productData = intent.extras!!.getString("product")
        product = Gson().fromJson(productData, Product::class.java)

        setupViews()
    }

    private fun setupViews() {
        binding.detailBack.setOnClickListener {
            finish()
        }

        binding.detailImage.load(product.urlToImage) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
            transformations(RoundedCornersTransformation(8f))
        }
        binding.detailTvTitle.text = product.name
        binding.detailTvAvailable.text = "موجودی کالا در انبار: ${product.price} عدد"
        binding.detailTvPrice.text = "${product.price} تومان"
        binding.detailTvDescription.text = product.description

        if (product.quantity == 0) {
            binding.detailTvAvailable.text = "اتمام موجودی"
            binding.detailTvAvailable.setTextColor(Color.parseColor("#F44336"))
            binding.detailTvPrice.visibility = View.GONE
            binding.detailOrderLayout.visibility = View.GONE
        }

        setupAddButton()
        setupRemoveButton()
        setupQuantityText()
    }

    private fun setupAddButton() {
        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            binding.detailAdd.setOnClickListener {
                val quantity = binding.detailQuantity.text.toString().toInt()
                if (quantity == 0) {
                    showLoading()
                    val addToCartCall = repository.addToCart(getUser().number, product.id)
                    addToCartCall.enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful && response.body() != null) {
                                if (response.body()!!.contains("done")) {
                                    binding.detailRemove.visibility = View.VISIBLE
                                    binding.detailQuantity.text = (quantity-1).toString()
                                } else {
                                    Toast.makeText(this@DetailActivity, "عملیات با خطا مواجه شد", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            }
                            showProduct()
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            showProduct()
                        }
                    })
                } else {
                    showLoading()
                    val updateCartCall = repository.updateCart(getUser().number, product.id, quantity+1)
                    updateCartCall.enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful && response.body() != null) {
                                if (response.body()!!.contains("done")) {
                                    binding.detailQuantity.text = (quantity+1).toString()
                                    if (quantity+1 == product.quantity) {
                                        binding.detailAdd.visibility = View.VISIBLE
                                    }
                                } else {
                                    Toast.makeText(this@DetailActivity, "عملیات با خطا مواجه شد", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            }
                            showProduct()
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            showProduct()
                        }
                    })
                }
            }
        }
    }

    private fun setupRemoveButton() {
        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            binding.detailRemove.setOnClickListener {
                val quantity = binding.detailQuantity.text.toString().toInt()
                if (quantity == 1) {
                    showLoading()
                    val removeFromCartCall = repository.removeFromCart(getUser().number, product.id)
                    removeFromCartCall.enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful && response.body() != null) {
                                if (response.body()!!.contains("done")) {
                                    binding.detailRemove.visibility = View.GONE
                                    binding.detailQuantity.text = (quantity-1).toString()
                                } else {
                                    Toast.makeText(this@DetailActivity, "عملیات با خطا مواجه شد", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            }
                            showProduct()
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            showProduct()
                        }
                    })
                } else {
                    showLoading()
                    val updateCartCall = repository.updateCart(getUser().number, product.id, quantity-1)
                    updateCartCall.enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful && response.body() != null) {
                                if (response.body()!!.contains("done")) {
                                    binding.detailQuantity.text = (quantity-1).toString()
                                } else {
                                    Toast.makeText(this@DetailActivity, "عملیات با خطا مواجه شد", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            }
                            showProduct()
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                            showProduct()
                        }
                    })
                }
            }
        }
    }

    private fun setupQuantityText() {
        showLoading()
        val getCartItemCall = repository.getCartItem(getUser().number, product.id)
        getCartItemCall.enqueue(object: Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body()!!
                    if (items.isNotEmpty()) {
                        binding.detailQuantity.text = items[0].quantity.toString()
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                }
                showProduct()
            }
            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                showProduct()
            }

        })
    }

    private fun isUserLoggedIn(): Boolean {
        val preferences = getSharedPreferences("sutech", MODE_PRIVATE)
        val userData = preferences.getString("user", "")
        return userData != ""
    }

    private fun getUser(): User {
        val preferences = getSharedPreferences("sutech", MODE_PRIVATE)
        val userData = preferences.getString("user", "")
        return Gson().fromJson(userData, User::class.java)
    }

    private fun showLoading() {
        binding.detailProduct.visibility = View.GONE
        binding.detailLoading.visibility = View.VISIBLE
    }

    private fun showProduct() {
        binding.detailProduct.visibility = View.VISIBLE
        binding.detailLoading.visibility = View.GONE
    }
}