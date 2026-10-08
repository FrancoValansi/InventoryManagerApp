package com.francovalansi.inventorymanager.data.repository

import com.francovalansi.inventorymanager.data.model.Inventory
import com.francovalansi.inventorymanager.data.remote.supabase
import io.github.jan.supabase.postgrest.from

class InventoryRepository {

    suspend fun getInventories(): List<Inventory> {
        return supabase
            .from("inventories")
            .select()
            .decodeList<Inventory>()
    }

    suspend fun createInventory(
        name: String,
        ownerId: String
    ) {
        supabase
            .from("inventories")
            .insert(
                Inventory(
                    name = name,
                    owner_id = ownerId
                )
            )
    }

    suspend fun deleteInventory(id: String) {
        supabase
            .from("inventories")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}