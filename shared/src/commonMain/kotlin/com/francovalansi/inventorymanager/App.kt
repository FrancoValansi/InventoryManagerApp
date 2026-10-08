package com.francovalansi.inventorymanager

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.francovalansi.inventorymanager.presentation.auth.AuthScreen
import com.francovalansi.inventorymanager.presentation.inventory.InventoryScreen

@Composable
fun App() {
    var loggedIn by remember { mutableStateOf(false) }

    MaterialTheme {
        if (loggedIn) {
            InventoryScreen()
        } else {
            AuthScreen(
                onLoggedIn = {
                    loggedIn = true
                }
            )
        }
    }
}