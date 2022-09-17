package com.sutechshop.model.sms

data class SendCode(
    val Mobile: String,
    val Footer: String = "فروشگاه سوتک"
)