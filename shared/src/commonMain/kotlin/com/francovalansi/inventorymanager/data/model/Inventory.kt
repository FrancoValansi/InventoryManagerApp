package com.francovalansi.inventorymanager.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Inventory(
    val id: Long? = null,
    val name: String,
    val owner_id: String,
    val created_at: String? = null
)