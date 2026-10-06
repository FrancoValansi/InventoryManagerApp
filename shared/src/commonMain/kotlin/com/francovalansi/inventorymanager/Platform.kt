package com.francovalansi.inventorymanager

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform