package com.thedeadpixelsociety.abacus.services

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * A read-only delegate that enables easily pulling services into properties from anywhere. Using this delegate from a
 * val property will result in a [MissingServiceException] if the requested service is not registered.
 * @param T The service type.
 * @param serviceClass The service [KClass].
 */
class ServiceDelegate<out T : Any>(private val serviceClass: KClass<T>) : ReadOnlyProperty<Any, T> {
    private var service: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (service == null) {
            service = fetchService(serviceClass)
        }

        return service ?: throw MissingServiceException(serviceClass)
    }
}