package com.sutechshop.model.query

data class Select(
    val access_key: String,
    val select: String,
    val where: String? = null,
    val limit: String? = null,
    val offset: String? = null
)