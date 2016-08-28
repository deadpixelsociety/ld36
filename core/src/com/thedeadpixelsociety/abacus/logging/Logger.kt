package com.thedeadpixelsociety.abacus.logging

import com.badlogic.gdx.utils.Disposable
import java.sql.Timestamp
import java.util.*

/**
 * Singleton logger interface for registering targets and writing messages.
 */
internal object Logger : Disposable {
    /**
     * The minimum allowed log level. Anything below this level will not be written at all.
     */
    var minLogLevel: LogLevel = LogLevel.TRACE
    /**
     * The log targets to write to.
     * @see LogTarget
     */
    val targets = com.badlogic.gdx.utils.Array<LogTarget>()

    /**
     * Writes a log message to all registered log targets.
     * @param logLevel The [LogLevel] of the message. If the specified log level is below the [minLogLevel] then no
     * message will be written.
     * @param tag An optional tag that describes where the message originated from.
     * @param message The log message itself.
     */
    fun write(logLevel: LogLevel, tag: String?, message: String) {
        if (targets.size == 0 || minLogLevel.ordinal > logLevel.ordinal) return
        val ts = Timestamp(Date().time)
        targets.forEach { it.write(ts, logLevel, tag, message) }
    }

    /**
     * Disposes of all log targets. This will call [LogTarget#flush] and [LogTarget#dispose] in order.
     */
    override fun dispose() {
        targets.forEach {
            it.flush()
            it.dispose()
        }

        targets.clear()
    }
}