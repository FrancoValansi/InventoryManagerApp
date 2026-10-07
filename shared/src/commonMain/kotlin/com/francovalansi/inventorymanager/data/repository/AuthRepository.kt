package com.francovalansi.inventorymanager.data.repository

import com.francovalansi.inventorymanager.data.remote.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun login(
        email: String,
        password: String
    ) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun logout() {
        supabase.auth.signOut()
    }
}