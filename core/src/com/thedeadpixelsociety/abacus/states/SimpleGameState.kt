package com.thedeadpixelsociety.abacus.states

/**
 * A simple (empty) implementation of [GameState]. Inherit from this to only override what you need.
 */
open class SimpleGameState : GameState {
    override val overlay = false

    override fun added() {
    }

    override fun removed() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun update(totalTime: Float, deltaTime: Float) {
    }

    override fun draw(totalTime: Float, deltaTime: Float) {
    }

    override fun dispose() {
    }
}