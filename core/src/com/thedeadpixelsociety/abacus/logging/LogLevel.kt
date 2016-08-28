package com.thedeadpixelsociety.abacus.logging

/**
 * Enumerates the available log levels. Each given log level is inclusive of the ones above it, meaning that the [TRACE]
 * log level will include messages from every other level as well. The [DEBUG] level would exclude [TRACE] but include
 * all others, and so on.
 */
enum class LogLevel {
    /**
     * The most inclusive log level. Shows every message.
     */
    TRACE,
    /**
     * Shows debug-related messages.
     */
    DEBUG,
    /**
     * Shows informational messages that aren't used for debugging.
     */
    INFO,
    /**
     * Shows non-fatal warnings messages.
     */
    WARN,
    /**
     * Shows critical error messages.
     */
    ERROR
}