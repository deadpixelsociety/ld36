package com.thedeadpixelsociety.abacus.services

import kotlin.reflect.KClass

/**
 * Convenience function to register a service from anywhere.
 * @param service The service to register.
 */
fun registerService(service: Any) = ServiceContainer.register(service)

/**
 * Disposes of all registered services. Should be called from your [com.badlogic.gdx.ApplicationListener] implementation.
 */
fun disposeServices() = ServiceContainer.dispose()

/**
 * Convenience function to fetch a service from anywhere.
 * @param T The service type.
 * @param serviceClass The service [KClass].
 * @return The service if found; otherwise, null.
 */
fun <T : Any> fetchService(serviceClass: KClass<T>): T? = ServiceContainer[serviceClass]

/**
 * Function to enable property delegation for fetching services (e.g., val myService by service(MyService::class).
 * @param T The service type.
 * @param serviceClass The service [KClass].
 * @see ServiceDelegate
 */
fun <T : Any> service(serviceClass: KClass<T>) = ServiceDelegate(serviceClass)
