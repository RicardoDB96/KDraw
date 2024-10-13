package com.ribod.kdraw

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform