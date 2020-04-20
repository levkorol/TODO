package com.totality

interface IScope {
    fun <T> registerService(serviceClass: Class<T>, service: T)
    fun <T> unregisterService(serviceClass: Class<T>, service: T)
    fun <T> getService(serviceClass: Class<T>): T
}
