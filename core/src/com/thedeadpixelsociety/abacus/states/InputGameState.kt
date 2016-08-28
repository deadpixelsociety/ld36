package com.thedeadpixelsociety.abacus.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor

/**
 * A simple (empty) implementation of [SimpleGameState] that also provides the [InputProcessor] interface to handle
 * input. This class will automatically register itself with [Gdx] as the input processor.
 */
open class InputGameState : SimpleGameState(), InputProcessor {
    init {
        Gdx.input.inputProcessor = this
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun keyTyped(character: Char) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun scrolled(amount: Int) = false

    override fun keyUp(keycode: Int) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun keyDown(keycode: Int) = false
}