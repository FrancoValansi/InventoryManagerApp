
package com.francovalansi.inventorymanager

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.francovalansi.inventorymanager.presentation.auth.AuthScreen
import com.francovalansi.inventorymanager.presentation.inventory.InventoryScreen
import com.francovalansi.inventorymanager.presentation.product.ProductScreen
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.francovalansi.inventorymanager.data.repository.AuthRepository

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface

@Composable
fun App() {
    // Controla si el usuario inició sesión.
    var loggedIn by remember { mutableStateOf(false) }

    // Guarda el inventario seleccionado para abrir sus productos.
    var selectedInventoryId by remember { mutableStateOf<String?>(null) }
    var selectedInventoryName by remember { mutableStateOf<String?>(null) }

    // Permite ejecutar operaciones suspendidas desde la interfaz.
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }

    // Cierra la sesión y vuelve a la pantalla de inicio.
    fun logout() {
        scope.launch {
            try {
                authRepository.logout()

                // Limpia el estado de navegación.
                selectedInventoryId = null
                selectedInventoryName = null
                loggedIn = false
            } catch (e: Exception) {
                // Si falla, mantenemos la sesión en la interfaz.
                e.printStackTrace()
            }
        }
    }

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            // Si no inició sesión, mostramos la pantalla de autenticación.
            if (!loggedIn) {
                AuthScreen(
                    onLoggedIn = {
                        loggedIn = true
                    }
                )
                // Si inició sesión pero no seleccionó un inventario,
                // mostramos la lista de inventarios.
            } else if (selectedInventoryId == null) {
                InventoryScreen(
                    onInventoryClick = { inventory ->
                        selectedInventoryId = inventory.id
                        selectedInventoryName = inventory.name
                    },
                    onLogout = {
                        logout()
                    }
                )
                // Si seleccionó un inventario, mostramos sus productos.
            } else {
                ProductScreen(
                    inventoryId = selectedInventoryId!!,
                    inventoryName = selectedInventoryName ?: "",
                    onBack = {
                        // Limpia la selección para volver a los inventarios.
                        selectedInventoryId = null
                        selectedInventoryName = null
                    }
                )
            }
        }
    }
}