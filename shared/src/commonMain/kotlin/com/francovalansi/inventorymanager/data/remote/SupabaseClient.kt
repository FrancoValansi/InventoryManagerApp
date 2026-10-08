package com.francovalansi.inventorymanager.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://dhjvduuvenuztbpmuyrn.supabase.co",
    supabaseKey = "sb_publishable_sZDhVdpK-uso_ZDjbEa6-w_mlwKVquO"
) {
    install(Auth) {
        scheme = "inventorymanager"
        host = "login-callback"
    }
    install(Postgrest)
}