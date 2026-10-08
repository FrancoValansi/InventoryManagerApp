package com.francovalansi.inventorymanager.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francovalansi.inventorymanager.data.model.Product
import com.francovalansi.inventorymanager.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProducts(inventoryId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                _products.value = repository.getProducts(inventoryId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun createProduct(
        inventoryId: String,
        name: String,
        price: Double,
        stock: Int,
        description: String
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.createProduct(
                    inventoryId = inventoryId,
                    name = name,
                    price = price,
                    stock = stock,
                    description = description
                )

                _products.value = repository.getProducts(inventoryId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProduct(
        id: String,
        inventoryId: String,
        name: String,
        price: Double,
        stock: Int,
        description: String
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.updateProduct(
                    id = id,
                    name = name,
                    price = price,
                    stock = stock,
                    description = description
                )

                // Actualiza la lista después de guardar los cambios.
                _products.value = repository.getProducts(inventoryId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteProduct(
        id: String,
        inventoryId: String
    ) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                repository.deleteProduct(id)

                _products.value = repository.getProducts(inventoryId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}