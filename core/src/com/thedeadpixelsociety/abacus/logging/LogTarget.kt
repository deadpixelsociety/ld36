package com.thedeadpixelsociety.abacus.logging

import com.badlogic.gdx.utils.Disposable
import java.sql.Timestamp

/**
 * Defines a log target that writes a log message to something.
 */
interface LogTarget : Disposable {
    /**
     * Flushes the target, if appropriate. Useful for buffered write operations and to ensure any left over buffer is
     * written during game close.
     */
    fun flush()

    /**
     * Writes the specified log message.
     * @param timestamp The [Timestamp] of when the message occurred.
     * @param logLevel The [LogLevel] of the message.
     * @param tag An optional tag that describes where the message originated from.
     * @param message The log message itself.
     */
    fun write(timestamp: Timestamp, logLevel: LogLevel, tag: String?, message: String)
}