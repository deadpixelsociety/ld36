package com.thedeadpixelsociety.abacus.states

import com.badlogic.gdx.utils.Disposable

/**
 * Defines a single game state that serves as an interface between the game's lifecycle and the game.
 */
interface GameState : Disposable {
    /**
     * Determines if the game state acts as an overlay or not. An overlay will not force states below it to be removed.
     */
    val overlay: Boolean

    /**
     * Called when the state is added to it's parent collection.
     */
    fun added()

    /**
     * Called when the state is removed from it's parent collection.
     */
    fun removed()

    /**
     * Called when the game's screen dimensions change.
     */
    fun resize(width: Int, height: Int)

    /**
     * Called when the game is paused.
     */
    fun pause()

    /**
     * Called when the game is resumed from a paused state.
     */
    fun resume()

    /**
     * Called when the game state should be updated.
     * @param totalTime The total amount of time, in seconds, that has elapsed since the game began.
     * @param deltaTime The amount of time, in seconds, since the last update.
     */
    fun update(totalTime: Float, deltaTime: Float)

    /**
     * Called when the game state should be rendered.
     * @param totalTime The total amount of time, in seconds, that has elapsed since the game began.
     * @param deltaTime The amount of time, in seconds, since the last update.
     */
    fun draw(totalTime: Float, deltaTime: Float)
}