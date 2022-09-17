package com.sutechshop.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.sutechshop.databinding.ActivityLoginBinding
import com.sutechshop.model.User
import com.sutechshop.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val repository = Repository()
    private var verificationCode = 0
    private var number = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        goToNumber()
        binding.loginBtnSend.setOnClickListener {
            val number = binding.loginInputMobile.text.toString()
            if (isNumberValid(number))
                sendVerifyCode(number)
            else
                Toast.makeText(this, "شماره موبایل اشتباه است", Toast.LENGTH_SHORT).show()
        }
        binding.loginBtnWrongNumber.setOnClickListener {
            goToNumber()
        }
        binding.loginBtnVerify.setOnClickListener {
            val inputCode = binding.loginInputVerify.text.toString()
            if (verificationCode == inputCode.toInt())
                validateVerificationCode(number.toString(), verificationCode)
            else
                Toast.makeText(this, "کد وارد شده اشتباه است!", Toast.LENGTH_SHORT).show()
        }
        binding.loginBtnFinish.setOnClickListener {
            val name = binding.loginInputName.text.toString()
            val gender =
                if (binding.loginRdoMan.isChecked) 1
                else 0
            if (name != "") {
                addUser(name, number, gender)
            }
        }
    }

    private fun sendVerifyCode(number: String) {
//        showLoading()
//
//        val sendVerifyCodeCall = repository.sendVerificationCode(number)
//        sendVerifyCodeCall.enqueue(object : Callback<Int> {
//            override fun onResponse(call: Call<Int>, response: Response<Int>) {
//                if (response.isSuccessful && response.body() != null) {
//                    try {
//                        verificationCode = response.body()!!
//                        goToVerify()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Int>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
//            }
//        })
        // TODO: DELETE THIS
        verificationCode = 1234
        goToVerify()
    }

    private fun validateVerificationCode(number: String, code: Int) {
//        showLoading()
//
//        val verifyCodeCall = repository.verifyCode(number, code.toString())
//        verifyCodeCall.enqueue(object : Callback<Int> {
//            override fun onResponse(call: Call<Int>, response: Response<Int>) {
//                if (response.isSuccessful && response.body() != null) {
//                    try {
//                        CoroutineScope(Dispatchers.IO).launch {
//                            val users = getUsers(number)
//                            if (users == null)
//                                goToName()
//                            else
//                                saveUserDataAndExit(users[0])
//                            Toast.makeText(
//                                applicationContext,
//                                "با موفقیت وارد شدید",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "خطا در برقراری ارتباط",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//            override fun onFailure(call: Call<Int>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
        // TODO: DELETE THIS
        this.number = number
        goToName()
    }

    private fun addUser(name: String, number: String, gender: Int) {
//        showLoading()
//
//        val addUserCall = repository.addUser(name, number, gender)
//        addUserCall.enqueue(object: Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful && response.body() != null) {
//                    val string = response.body()!!
//                    if (string.contains("done")) {
//                        val user = User(name, number, gender)
//                        saveUserDataAndExit(user)
//                    } else {
//                        Toast.makeText(this@LoginActivity, "عملیات موفقیت امیز نبود", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                t.printStackTrace()
//                Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
//            }
//        })
        val user = User(name, number, gender)
        saveUserDataAndExit(user)
    }

    private fun getUsers(number: String): List<User>? {
        val users: MutableList<User>? = null
        val getUsersCall = repository.getUsers(number)
        val response = getUsersCall.execute()
        if (response.isSuccessful && response.body() != null) {
            try {
                users?.addAll(response.body()!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this@LoginActivity, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show()
        }
        return users
    }

    private fun isNumberValid(number: String): Boolean {
        return Pattern.compile("(0)?9\\d{9}").matcher(number).matches()
    }

    private fun saveUserDataAndExit(user: User) {
        getSharedPreferences("sutech", MODE_PRIVATE)
            .edit()
            .putString("user", Gson().toJson(user))
            .apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun showLoading() {
        binding.loginNumberLayout.visibility = View.GONE
        binding.loginCodeLayout.visibility = View.GONE
        binding.loginNameLayout.visibility = View.GONE
        binding.loginLoading.visibility = View.VISIBLE
    }

    private fun goToNumber() {
        binding.loginNumberLayout.visibility = View.VISIBLE
        binding.loginCodeLayout.visibility = View.GONE
        binding.loginNameLayout.visibility = View.GONE
        binding.loginLoading.visibility = View.GONE
    }

    private fun goToVerify() {
        binding.loginNumberLayout.visibility = View.GONE
        binding.loginCodeLayout.visibility = View.VISIBLE
        binding.loginNameLayout.visibility = View.GONE
        binding.loginLoading.visibility = View.GONE
    }

    private fun goToName() {
        binding.loginNumberLayout.visibility = View.GONE
        binding.loginCodeLayout.visibility = View.GONE
        binding.loginNameLayout.visibility = View.VISIBLE
        binding.loginLoading.visibility = View.GONE
    }
}