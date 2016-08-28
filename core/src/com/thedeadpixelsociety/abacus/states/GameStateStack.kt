package com.thedeadpixelsociety.abacus.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Disposable
import java.util.*

/**
 * Provides a stack-based implementation for handling game states. Each of the this class' functions should be called
 * appropriately from your [com.badlogic.gdx.ApplicationListener] implementation.
 */
open class GameStateStack : Disposable {
    protected val states = Stack<GameState>()

    /**
     * Adds the specified [state] to the top of the stack. This will call [GameState#added] and [GameState#resize] in order.
     * @param state The [GameState] to add.
     * @see GameState#added
     * @see GameState#resize
     */
    fun add(state: GameState) {
        states.push(state)
        state.added()
        state.resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    /**
     * Removes the specified [state] from the stack. This will call [GameState#removed] and [GameState#dispose] in order.
     * @param state The [GameState] to remove.
     * @return true if the state was successfully removed; otherwise, false.
     * @see GameState#removed
     * @see GameState#dispose
     */
    fun remove(state: GameState): Boolean {
        val index = states.indexOf(state)
        if (index != -1) {
            states.removeAt(index)
            state.removed()
            state.dispose()
            return true
        }

        return false
    }

    /**
     * Resize each active game state.
     * @param width The game's window width in pixels.
     * @param height The game's window height in pixels.
     * @see GameState#resize
     */
    fun resize(width: Int, height: Int) = states.forEach { it.resize(width, height) }

    /**
     * Pauses each active game state.
     * @see GameState#pause
     */
    fun pause() = states.forEach { it.pause() }

    /**
     * Resumes each active game state from a paused state.
     * @see GameState#resume
     */
    fun resume() = states.forEach { it.resume() }

    /**
     * Updates each active game state starting from the top-most to the bottom-most. If a state is below a non-overlay
     * state it will automatically be removed.
     * @param totalTime The total amount of time, in seconds, that has elapsed since the game began.
     * @param deltaTime The amount of time, in seconds, since the last update.
     * @see GameState#overlay
     * @see GameState#update
     */
    fun update(totalTime: Float, deltaTime: Float) {
        var top = true
        for (i in states.size - 1 downTo 0) {
            val state = states[i]
            state.update(totalTime, deltaTime)
            if (!top) remove(state)
            if (!state.overlay) top = false
        }
    }

    /**
     * Renders each active game state starting from bottom-most to top-most.
     * @param totalTime The total amount of time, in seconds, that has elapsed since the game began.
     * @param deltaTime The amount of time, in seconds, since the last update.
     * @see GameState#draw
     */
    fun draw(totalTime: Float, deltaTime: Float) = states.forEach { it.draw(totalTime, deltaTime) }

    /**
     * Disposes of each active game state. This will call [GameState#removed] and [GameState#dispose] in order.
     * @see GameState#removed
     * @see GameState#dispose
     */
    override fun dispose() {
        states.forEach {
            it.removed()
            it.dispose()
        }

        states.clear()
    }
}