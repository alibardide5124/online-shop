package com.sutechshop.model.query

data class Delete(
    val access_key: String,
    val delete: String,
    val where: String
)