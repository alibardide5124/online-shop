package com.sutechshop.model

data class Cart(
    val userId: String,
    val productId: Int,
    val quantity: Int
)