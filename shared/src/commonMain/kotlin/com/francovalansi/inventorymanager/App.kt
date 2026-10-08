package com.francovalansi.inventorymanager

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.francovalansi.inventorymanager.presentation.auth.AuthScreen
import com.francovalansi.inventorymanager.presentation.inventory.InventoryScreen
import com.francovalansi.inventorymanager.presentation.product.ProductScreen

@Composable
fun App() {
    // Controla si el usuario inició sesión.
    var loggedIn by remember { mutableStateOf(false) }

    // Guarda el inventario seleccionado para abrir sus productos.
    var selectedInventoryId by remember { mutableStateOf<String?>(null) }
    var selectedInventoryName by remember { mutableStateOf<String?>(null) }

    MaterialTheme {

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
                }
            )

            // Si seleccionó un inventario, mostramos sus productos.
        } else {
            ProductScreen(
                inventoryId = selectedInventoryId!!,
                inventoryName = selectedInventoryName ?: ""
            )
        }
    }
}