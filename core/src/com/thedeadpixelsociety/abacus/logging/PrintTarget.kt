package com.thedeadpixelsociety.abacus.logging

import java.sql.Timestamp

/**
 * A simple implementation of [LogTarget] that just outputs to standard out.
 */
open class PrintTarget : LogTarget {
    override fun flush() {
        // nop
    }

    override fun write(timestamp: Timestamp, logLevel: LogLevel, tag: String?, message: String) {
        println("[$logLevel:$timestamp]//${tag.orEmpty()} $message")
    }

    override fun dispose() {
        // nop
    }
}