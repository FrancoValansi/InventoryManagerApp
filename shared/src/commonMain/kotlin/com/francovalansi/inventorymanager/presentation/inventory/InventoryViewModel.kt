package com.francovalansi.inventorymanager.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francovalansi.inventorymanager.data.model.Inventory
import com.francovalansi.inventorymanager.data.remote.supabase
import com.francovalansi.inventorymanager.data.repository.InventoryRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventoryViewModel : ViewModel() {

    private val repository = InventoryRepository()

    private val _inventories = MutableStateFlow<List<Inventory>>(emptyList())
    val inventories: StateFlow<List<Inventory>> = _inventories

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadInventories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                _inventories.value = repository.getInventories()

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun createInventory(name: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val user = supabase.auth.currentUserOrNull()
                    ?: throw Exception("Usuario no autenticado")

                repository.createInventory(
                    name = name,
                    ownerId = user.id
                )

                _inventories.value = repository.getInventories()

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteInventory(id: Long) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.deleteInventory(id)

                _inventories.value = repository.getInventories()

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}