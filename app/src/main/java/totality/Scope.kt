package com.totality

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

class Scope : IScope {

    private val _services = HashMap<Type, Any>()

    override fun <T> registerService(serviceClass: Class<T>, service: T) {
        _services[serviceClass] = service as Any
    }

    override fun <T> unregisterService(serviceClass: Class<T>, service: T) {
        if (_services.containsKey(serviceClass) && _services[serviceClass] === service) {
            _services.remove(serviceClass)
        }
    }

    override fun <T> getService(serviceClass: Class<T>): T {
        return _services[serviceClass] as T
    }
}
