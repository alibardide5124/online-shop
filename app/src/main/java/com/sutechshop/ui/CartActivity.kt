package com.sutechshop.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sutechshop.R
import java.util.prefs.Preferences

class CartActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences("sutech", MODE_PRIVATE)
        if (!isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setContentView(R.layout.activity_cart)
    }

    private fun isUserLoggedIn(): Boolean {
        val userData = preferences.getString("user", "")
        return userData != ""
    }
}