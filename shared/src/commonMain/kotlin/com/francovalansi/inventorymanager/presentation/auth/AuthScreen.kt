package com.francovalansi.inventorymanager.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(
    onLoggedIn: () -> Unit
) {
    var isRegistering by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val viewModel = remember { AuthViewModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegistering) "Crear cuenta" else "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                message = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                message = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                loading = true

                if (isRegistering) {
                    viewModel.register(email, password) { success, error ->
                        loading = false

                        if (success) {
                            message =
                                "Cuenta creada. Revisá tu email si Supabase pide confirmación."
                        } else {
                            message = error ?: "Error al crear la cuenta."
                        }
                    }
                } else {
                    viewModel.login(email, password) { success, error ->
                        loading = false

                        if (success) {
                            onLoggedIn()
                        } else {
                            message = error ?: "Error al iniciar sesión."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading &&
                    email.isNotBlank() &&
                    password.isNotBlank()
        ) {
            Text(
                text = when {
                    loading -> "Cargando..."
                    isRegistering -> "Crear cuenta"
                    else -> "Iniciar sesión"
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                isRegistering = !isRegistering
                message = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isRegistering) {
                    "Ya tengo una cuenta"
                } else {
                    "Crear una cuenta"
                }
            )
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}