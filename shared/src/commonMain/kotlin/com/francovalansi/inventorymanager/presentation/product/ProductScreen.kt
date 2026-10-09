
package com.francovalansi.inventorymanager.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.francovalansi.inventorymanager.data.model.Product

@Composable
fun ProductScreen(
    inventoryId: String,
    inventoryName: String,
    onBack: () -> Unit,
    viewModel: ProductViewModel = remember { ProductViewModel() }
) {
    // Observa la lista, el estado de carga y los errores del ViewModel.
    val products by viewModel.products.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Controla los formularios y el producto que se está editando o eliminando.
    var showForm by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Product?>(null) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    // Valores de los campos del formulario.
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Carga los productos del inventario seleccionado.
    LaunchedEffect(inventoryId) {
        viewModel.loadProducts(inventoryId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Permite volver a la lista de inventarios.
        TextButton(
            onClick = {
                onBack()
            }
        ) {
            Text("← Volver a Inventarios")
        }

        Text(
            text = inventoryName,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Abre un formulario vacío para crear un producto.
        Button(
            onClick = {
                productToEdit = null
                name = ""
                price = ""
                stock = ""
                description = ""
                showForm = true
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nuevo producto")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Muestra el estado de carga y los posibles errores.
        if (loading) {
            CircularProgressIndicator()
        }

        if (error != null) {
            Text(
                text = "Error: ${error}",
                color = MaterialTheme.colorScheme.error
            )
        }

        // Lista los productos ordenados alfabéticamente.
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = products.sortedBy { it.name.lowercase() },
                key = { it.id ?: "${it.inventory_id}-${it.name}" }
            ) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = "Precio: ${product.price}")
                        Text(text = "Stock: ${product.stock}")

                        if (!product.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = product.description)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Permite editar un producto cargando sus valores actuales.
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    productToEdit = product
                                    name = product.name
                                    price = product.price.toString()
                                    stock = product.stock.toString()
                                    description = product.description.orEmpty()
                                    showForm = true
                                },
                                enabled = !loading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Editar")
                            }

                            // Solicita confirmación antes de eliminar.
                            OutlinedButton(
                                onClick = {
                                    productToDelete = product
                                },
                                enabled = !loading,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    // Formulario reutilizado para crear y editar productos.
    if (showForm) {
        AlertDialog(
            onDismissRequest = {
                showForm = false
                productToEdit = null
            },
            title = {
                Text(
                    if (productToEdit == null) {
                        "Nuevo producto"
                    } else {
                        "Editar producto"
                    }
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Precio") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = { Text("Stock") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = price.toDoubleOrNull()
                        val parsedStock = stock.toIntOrNull()

                        // Ejecuta la operación correspondiente según el formulario.
                        if (parsedPrice != null &&
                            parsedStock != null &&
                            name.isNotBlank() &&
                            parsedPrice >= 0 &&
                            parsedStock >= 0
                        ) {
                            val existingProduct = productToEdit

                            if (existingProduct == null) {
                                viewModel.createProduct(
                                    inventoryId = inventoryId,
                                    name = name.trim(),
                                    price = parsedPrice,
                                    stock = parsedStock,
                                    description = description.trim()
                                )
                            } else {
                                val productId = existingProduct.id

                                if (productId != null) {
                                    viewModel.updateProduct(
                                        id = productId,
                                        inventoryId = inventoryId,
                                        name = name.trim(),
                                        price = parsedPrice,
                                        stock = parsedStock,
                                        description = description.trim()
                                    )
                                }
                            }

                            showForm = false
                            productToEdit = null
                        }
                    },
                    enabled = name.isNotBlank() &&
                            (price.toDoubleOrNull()?.let { it >= 0 } == true) &&
                            (stock.toIntOrNull()?.let { it >= 0 } == true)
                ) {
                    Text(
                        if (productToEdit == null) "Crear" else "Editar"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showForm = false
                        productToEdit = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirma la eliminación antes de llamar al ViewModel.
    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = {
                productToDelete = null
            },
            title = {
                Text("Eliminar producto")
            },
            text = {
                Text(
                    "¿Seguro que querés eliminar \"${product.name}\"? " +
                            "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val productId = product.id

                        if (productId != null) {
                            viewModel.deleteProduct(
                                id = productId,
                                inventoryId = inventoryId
                            )
                        }

                        productToDelete = null
                    },
                    enabled = !loading
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        productToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}