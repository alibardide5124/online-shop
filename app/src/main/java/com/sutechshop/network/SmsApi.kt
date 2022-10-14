package com.sutechshop.network

import com.sutechshop.model.sms.SendCode
import com.sutechshop.model.sms.VerifyCode
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SmsApi {

    @Headers(***REMOVED***)
    @POST("SendAutoCode/")
    fun sendVerificationCode(@Body sendCode: SendCode): Call<Int>

    @Headers(***REMOVED***)
    @POST("CheckSendCode/")
    fun verifyCode(@Body verifyCode: VerifyCode): Call<Boolean>

}