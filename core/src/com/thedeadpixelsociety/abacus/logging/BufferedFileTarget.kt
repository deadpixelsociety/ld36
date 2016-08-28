package com.thedeadpixelsociety.abacus.logging

import com.badlogic.gdx.files.FileHandle
import com.thedeadpixelsociety.abacus.logging.BufferedFileTarget.Companion.BUFFER_SIZE
import java.sql.Timestamp

/**
 * An implementation of [LogTarget] that accumulates messages in a buffer and once full writes the buffer to a file.
 * Since this target involves writing to disk pretty regularly it should only be used for debugging or with a high
 * buffer size to reduce writes.
 * @param fileHandle The [com.badlogic.gdx.files.FileHandle] to write to.
 * @param bufferSize The buffer size to maintain in bytes. Defaults to [BUFFER_SIZE].
 */
open class BufferedFileTarget(
    private val fileHandle: FileHandle,
    private val bufferSize: Int = BufferedFileTarget.BUFFER_SIZE
) : LogTarget {
    companion object {
        const val BUFFER_SIZE = 8096
    }

    private var buffer = StringBuilder(bufferSize)

    override fun flush() {
        try {
            fileHandle.writeString(buffer.toString(), true)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun write(timestamp: Timestamp, logLevel: LogLevel, tag: String?, message: String) {
        buffer.appendln("[$logLevel:$timestamp]//${tag.orEmpty()} $message")

        if (buffer.length >= bufferSize) {
            flush()
            buffer = StringBuilder(bufferSize)
        }
    }

    override fun dispose() {
        // nop
    }
}