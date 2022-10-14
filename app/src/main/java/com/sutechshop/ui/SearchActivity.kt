package com.sutechshop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.sutechshop.adapter.ProductListAdapter
import com.sutechshop.databinding.ActivitySearchBinding
import com.sutechshop.model.Product
import com.sutechshop.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.searchBack.setOnClickListener { finish() }
        binding.searchInput.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.searchBtnRetry.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        showLoading()
        val text = binding.searchInput.text.toString()

        val searchProductsCall = repository.searchProducts(text)
        searchProductsCall.enqueue(object: Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null) {
                    val adapter = ProductListAdapter(this@SearchActivity, response.body()!!)
                    binding.searchRv.layoutManager = LinearLayoutManager(this@SearchActivity)
                    binding.searchRv.adapter = adapter

                    if (response.body()!!.isNotEmpty())
                        showRecyclerView()
                    else
                        showEmpty()
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                t.printStackTrace()
                showError()
            }

        })
    }

    private fun showRecyclerView() {
        binding.searchRv.visibility = View.VISIBLE
        binding.searchLoading.visibility = View.GONE
        binding.searchRetry.visibility = View.GONE
        binding.searchTxtEmpty.visibility = View.GONE
    }

    private fun showLoading() {
        binding.searchRv.visibility = View.GONE
        binding.searchLoading.visibility = View.VISIBLE
        binding.searchRetry.visibility = View.GONE
        binding.searchTxtEmpty.visibility = View.GONE
    }

    private fun showError() {
        binding.searchRv.visibility = View.GONE
        binding.searchLoading.visibility = View.GONE
        binding.searchRetry.visibility = View.VISIBLE
        binding.searchTxtEmpty.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.searchRv.visibility = View.GONE
        binding.searchLoading.visibility = View.GONE
        binding.searchRetry.visibility = View.GONE
        binding.searchTxtEmpty.visibility = View.VISIBLE
    }


}