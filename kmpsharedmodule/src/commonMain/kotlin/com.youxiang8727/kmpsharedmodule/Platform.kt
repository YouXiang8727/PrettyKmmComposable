package com.youxiang8727.kmpsharedmodule

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


