package com.sutechshop.network

import com.sutechshop.model.sms.SendCode
import com.sutechshop.model.sms.VerifyCode
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SmsApi {

    @POST("SendAutoCode/")
    fun sendVerificationCode(@Body sendCode: SendCode): Call<Int>

    @POST("CheckSendCode/")
    fun verifyCode(@Body verifyCode: VerifyCode): Call<Boolean>

}