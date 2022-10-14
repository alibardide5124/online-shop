package com.sutechshop.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.sutechshop.R
import com.sutechshop.databinding.ActivityUserPanelBinding
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPanelBinding
    private lateinit var preferences: SharedPreferences
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("sutech", MODE_PRIVATE)

        setupViews()
    }

    private fun setupViews() {
        val userJson = preferences.getString("user", "")
        val user = Gson().fromJson(userJson, User::class.java)

        binding.panelBack.setOnClickListener {
            finish()
        }

        binding.panelInputName.editText!!.setText(user.name)
        binding.panelInputMobile.editText!!.setText(user.number)
        binding.panelInputMobile.editText!!.isEnabled = false
        if (user.gender == 1)
            binding.panelRdoMan.isChecked = true
        else
            binding.panelRdoWoman.isChecked = true

        binding.panelButtonSubmit.setOnClickListener {
            val name = binding.panelInputName.editText!!.text.toString()
            val number = binding.panelInputMobile.editText!!.text.toString()
            val gender =
                if (binding.panelRdoMan.isChecked) 1
                else 0
            submitChanges(User(name, number, gender))
        }
        binding.panelButtonDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("حذف حساب کاربری")
                .setMessage("آیا از حذف حساب کاربری مطمئن هستید؟")
                .setPositiveButton("بله") { dialogInterface: DialogInterface, i: Int ->
                    deleteUser(user)
                }
                .setNegativeButton("لغو", null)
                .create()
                .show()
        }
    }

    private fun submitChanges(user: User) {
        val setUserCall = repository.setUser(user.name, user.number, user.gender)
        setUserCall.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val string = response.body()!!
                    if (string.contains("done")) {
                        saveUserDataAndExit(user)
                    } else {
                        Toast.makeText(this@UserPanelActivity, "عملیات موفقیت امیز نبود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@UserPanelActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@UserPanelActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteUser(user: User) {
        val deleteUserCall = repository.deleteUser(user.number)
        deleteUserCall.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null) {
                    val string = response.body()!!
                    if (string.contains("done")) {
                        deleteUserDataAndExit()
                    } else {
                        Toast.makeText(this@UserPanelActivity, "عملیات موفقیت امیز نبود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@UserPanelActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@UserPanelActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserDataAndExit(user: User) {
        preferences.edit().putString("user", Gson().toJson(user)).apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent)
    }

    private fun deleteUserDataAndExit() {
        preferences.edit().putString("user", "").apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent)
    }

}