package com.thedeadpixelsociety.abacus.logging

/**
 * Disposes of all registered loggers. Should be called from your [com.badlogic.gdx.ApplicationListener] implementation.
 */
fun disposeLogger() = Logger.dispose()

/**
 * Specifies the log targets to use.
 * @param targets One or more log targets to use.
 * @see LogTarget
 */
fun logTargets(vararg targets: LogTarget) = Logger.targets.addAll(*targets)

/**
 * Specifies the minimum log level. Any message with a level below this will not be written.
 * @param logLevel The minimum [LogLevel].
 */
fun logLevel(logLevel: LogLevel) {
    Logger.minLogLevel = logLevel
}

/**
 * Convenience function to write a [LogLevel#TRACE] level message with an optional tag.
 * @param message The log message.
 * @param tag An optional tag that describes where the message originated from.
 */
fun trace(message: String, tag: String? = null) = Logger.write(LogLevel.TRACE, tag, message)

/**
 * Convenience function to write a [LogLevel#DEBUG] level message with an optional tag.
 * @param message The log message.
 * @param tag An optional tag that describes where the message originated from.
 */
fun debug(message: String, tag: String? = null) = Logger.write(LogLevel.DEBUG, tag, message)

/**
 * Convenience function to write a [LogLevel#INFO] level message with an optional tag.
 * @param message The log message.
 * @param tag An optional tag that describes where the message originated from.
 */
fun info(message: String, tag: String? = null) = Logger.write(LogLevel.INFO, tag, message)

/**
 * Convenience function to write a [LogLevel#WARN] level message with an optional tag.
 * @param message The log message.
 * @param tag An optional tag that describes where the message originated from.
 */
fun warn(message: String, tag: String? = null) = Logger.write(LogLevel.WARN, tag, message)

/**
 * Convenience function to write a [LogLevel#ERROR] level message with an optional tag.
 * @param message The log message.
 * @param tag An optional tag that describes where the message originated from.
 */
fun error(message: String, tag: String? = null) = Logger.write(LogLevel.ERROR, tag, message)
