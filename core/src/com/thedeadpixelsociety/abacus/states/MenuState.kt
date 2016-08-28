package com.thedeadpixelsociety.abacus.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.thedeadpixelsociety.abacus.Sprites
import com.thedeadpixelsociety.abacus.services.service

class MenuState : InputGameState() {
    companion object {
        const val VIEWPORT_WIDTH = 1920f
        const val VIEWPORT_HEIGHT = 1080f
    }

    private val viewport by lazy { FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT) }
    private val spriteBatch by service(SpriteBatch::class)
    private val stateStack by service(GameStateStack::class)

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        viewport.camera.position.set(VIEWPORT_WIDTH * .5f, VIEWPORT_HEIGHT * .5f, 0f)
    }

    override fun draw(totalTime: Float, deltaTime: Float) {
        spriteBatch.projectionMatrix = viewport.camera.combined
        spriteBatch.begin()
        Sprites.title.setPosition(
            (VIEWPORT_WIDTH - Sprites.title.width) * .5f,
            (VIEWPORT_HEIGHT - Sprites.title.height) * .5f
        )
        Sprites.title.draw(spriteBatch)
        spriteBatch.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.ENTER -> {
                stateStack.add(PlayState())
                return true
            }
            Input.Keys.F11 -> {
                if (Gdx.graphics.isFullscreen) {
                    Gdx.graphics.setWindowedMode(
                        (Gdx.graphics.displayMode.width * .66f).toInt(),
                        (Gdx.graphics.displayMode.height * .66f).toInt()
                    )
                } else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
                }
            }
            Input.Keys.ESCAPE -> {
                Gdx.app.exit()
            }
        }

        return false
    }
}