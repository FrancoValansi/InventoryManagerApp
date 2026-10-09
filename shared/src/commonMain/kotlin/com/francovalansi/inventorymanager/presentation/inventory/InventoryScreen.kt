package com.francovalansi.inventorymanager.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.francovalansi.inventorymanager.data.model.Inventory
import com.francovalansi.inventorymanager.presentation.components.ConfirmationDialog
import com.francovalansi.inventorymanager.presentation.components.FormField

@Composable
fun InventoryScreen(
    onInventoryClick: (Inventory) -> Unit,
    onLogout: () -> Unit,
    viewModel: InventoryViewModel = remember { InventoryViewModel() }
) {
    val inventories by viewModel.inventories.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showCreateForm by remember { mutableStateOf(false) }
    var inventoryToDelete by remember { mutableStateOf<Inventory?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadInventories()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Encabezado con el título y el menú de cuenta.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis inventarios")

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.End) {
                TextButton(onClick = { showMenu = true }) {
                    Text("Mi cuenta  ☰")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            showMenu = false
                            showLogoutConfirmation = true
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                name = ""
                showCreateForm = true
            }
        ) {
            Text("Nuevo inventario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Error: $it")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(inventories.sortedBy { it.name.lowercase() }) { inventory ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onInventoryClick(inventory)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(inventory.name)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            inventoryToDelete = inventory
                        }
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }

    // Formulario para crear un inventario.
    if (showCreateForm) {
        AlertDialog(
            onDismissRequest = { showCreateForm = false },
            title = { Text("Nuevo inventario") },
            text = {
                FormField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre del inventario"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createInventory(name.trim())
                        showCreateForm = false
                    },
                    enabled = name.isNotBlank()
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateForm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmación antes de cerrar la sesión.
    if (showLogoutConfirmation) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmation = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Querés cerrar tu sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmation = false
                        onLogout()
                    }
                ) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutConfirmation = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmación antes de eliminar un inventario.
    inventoryToDelete?.let { inventory ->
        ConfirmationDialog(
            title = "Eliminar inventario",
            message = "¿Estás seguro de que querés eliminar \"${inventory.name}\"?",
            onConfirm = {
                inventory.id?.let { id ->
                    viewModel.deleteInventory(id)
                }
                inventoryToDelete = null
            },
            onDismiss = {
                inventoryToDelete = null
            }
        )
    }
}