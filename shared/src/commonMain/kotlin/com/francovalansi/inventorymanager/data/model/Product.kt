package com.francovalansi.inventorymanager.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String? = null,
    val inventory_id: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val description: String? = null,
    val created_at: String? = null
)