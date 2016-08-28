package com.thedeadpixelsociety.abacus.services

import kotlin.reflect.KClass

/**
 * An exception that is thrown when a requested service is not available.
 * @param serviceClass The service [KClass].
 */
class MissingServiceException(serviceClass: KClass<*>)
: RuntimeException("No service registered for class '${serviceClass.simpleName}'.") {
}