package com.francovalansi.inventorymanager.data.repository

import com.francovalansi.inventorymanager.data.model.Product
import com.francovalansi.inventorymanager.data.remote.supabase
import io.github.jan.supabase.postgrest.from

class ProductRepository {

    suspend fun getProducts(inventoryId: String): List<Product> {
        return supabase
            .from("products")
            .select {
                filter {
                    eq("inventory_id", inventoryId)
                }
            }
            .decodeList<Product>()
    }

    suspend fun createProduct(
        inventoryId: String,
        name: String,
        price: Double,
        stock: Int,
        description: String
    ) {
        supabase
            .from("products")
            .insert(
                Product(
                    inventory_id = inventoryId,
                    name = name,
                    price = price,
                    stock = stock,
                    description = description
                )
            )
    }

    suspend fun updateProduct(
        id: String,
        name: String,
        price: Double,
        stock: Int,
        description: String
    ) {
        supabase
            .from("products")
            .update(
                {
                    set("name", name)
                    set("price", price)
                    set("stock", stock)
                    set("description", description)
                }
            ) {
                filter {
                    eq("id", id)
                }
            }
    }

    suspend fun deleteProduct(id: String) {
        supabase
            .from("products")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}