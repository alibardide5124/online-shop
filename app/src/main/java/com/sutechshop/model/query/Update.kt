package com.sutechshop.model.query

data class Update(
    val access_key: String,
    val update: String,
    val set: String,
    val where: String
)