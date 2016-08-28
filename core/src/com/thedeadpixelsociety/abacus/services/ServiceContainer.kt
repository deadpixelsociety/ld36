package com.thedeadpixelsociety.abacus.services

import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectMap
import kotlin.reflect.KClass

/**
 * Singleton container that manages game-wide service objects.
 */
internal object ServiceContainer : Disposable {
    val services = ObjectMap<String, Any>()

    /**
     * Registers the specified [service].
     * @param service The service object to add.
     */
    fun register(service: Any) {
        services.put(service.javaClass.name, service)
    }

    /**
     * Retrieves a service object by it's [KClass] type.
     * @param T The service type.
     * @param serviceClass The service [KClass].
     * @return The service class as the specified type.
     * @throws MissingServiceException if the specified service has not been previously registered.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(serviceClass: KClass<T>): T? {
        return services[serviceClass.java.name] as? T
    }

    /**
     * Disposes of each registered service if it implements [Disposable]
     */
    override fun dispose() {
        services.mapNotNull { it.value as? Disposable }.forEach { it.dispose() }
        services.clear()
    }
}