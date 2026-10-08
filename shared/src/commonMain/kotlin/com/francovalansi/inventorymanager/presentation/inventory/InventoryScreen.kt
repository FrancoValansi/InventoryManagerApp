package com.francovalansi.inventorymanager.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.francovalansi.inventorymanager.presentation.components.FormField

@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = remember { InventoryViewModel() }
) {
    val inventories by viewModel.inventories.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showCreateForm by remember {
        mutableStateOf(false)
    }

    var name by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        viewModel.loadInventories()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Mis Inventarios")

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

                Button(
                    onClick = {
                        // Más adelante: abrir productos
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(inventory.name)
                }
            }
        }
    }

    if (showCreateForm) {
        AlertDialog(
            onDismissRequest = {
                showCreateForm = false
            },
            title = {
                Text("Nuevo inventario")
            },
            text = {
                FormField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
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
                Button(
                    onClick = {
                        showCreateForm = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}