package com.sutechshop.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
    val description: String?,
    val price: Int,
    val urlToImage: String?
)