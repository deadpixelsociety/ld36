package com.thedeadpixelsociety.abacus.states

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.thedeadpixelsociety.abacus.Player
import com.thedeadpixelsociety.abacus.Sounds
import com.thedeadpixelsociety.abacus.systems.*

class PlayState : InputGameState() {
    companion object {
        const val VIEWPORT_WIDTH = 1920f
        const val VIEWPORT_HEIGHT = 1080f
        const val DOUBLE_TAP_TIME = .19f
        const val SMALL_SHAKE_DURATION = .25f
        const val SMALL_SHAKE_INTENSITY = 8f
        const val LARGE_SHAKE_DURATION = .35f
        const val LARGE_SHAKE_INTENSITY = 24f
        const val MASSIVE_SHAKE_DURATION = .75f
        const val MASSIVE_SHAKE_INTENSITY = 64f
    }

    enum class Shake {
        SMALL,
        LARGE,
        MASSIVE
    }

    private val viewport by lazy { FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT) }
    private val engine by lazy { Engine() }
    private var p1DoubleTime = DOUBLE_TAP_TIME
    private var p2DoubleTime = DOUBLE_TAP_TIME
    private var p1DoubleTap = false
    private var p2DoubleTap = false
    private var shakeTime = 0f
    private var shakeIntensity = 0f
    private val defaultCameraPos = Vector2()

    override fun added() {
        engine.addSystem(GroupSystem())
        engine.addSystem(TagSystem())
        engine.addSystem(TaskSystem())
        engine.addSystem(AbacusSystem(VIEWPORT_WIDTH, VIEWPORT_HEIGHT))
        engine.addSystem(SpriteRenderSystem(viewport, VIEWPORT_WIDTH, VIEWPORT_HEIGHT))
        engine.addSystem(ParticleRenderSystem(viewport, VIEWPORT_WIDTH, VIEWPORT_HEIGHT))
        engine.addSystem(TextRenderSystem(viewport, VIEWPORT_WIDTH, VIEWPORT_HEIGHT))

        Sounds.playMusic()
    }

    override fun removed() {
        Sounds.music.stop()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        viewport.camera.position.set(VIEWPORT_WIDTH * .5f, VIEWPORT_HEIGHT * .5f, 0f)
    }

    override fun update(totalTime: Float, deltaTime: Float) {
        if (p1DoubleTap) {
            p1DoubleTime -= deltaTime
            if (p1DoubleTime <= 0f) {
                p1DoubleTap = false
                p1DoubleTime = 0f
                val abacus = engine.getSystem(AbacusSystem::class.java)
                abacus.removeBead(Player.ONE)
            }
        }

        if (p2DoubleTap) {
            p2DoubleTime -= deltaTime
            if (p2DoubleTime <= 0f) {
                p2DoubleTap = false
                p2DoubleTime = 0f
                val abacus = engine.getSystem(AbacusSystem::class.java)
                abacus.removeBead(Player.TWO)
            }
        }

        if (shakeTime > 0f) {
            shakeTime -= deltaTime
            viewport.camera.position.set(defaultCameraPos.x, defaultCameraPos.y, 0f)
            viewport.camera.position.add(MathUtils.random() * shakeIntensity, MathUtils.random() * shakeIntensity, 0f)
            if (shakeTime <= 0f) {
                shakeTime = 0f
                viewport.camera.position.set(defaultCameraPos.x, defaultCameraPos.y, 0f)
            }
        }

        engine.update(deltaTime)
    }

    override fun draw(totalTime: Float, deltaTime: Float) {
        engine.getSystem(SpriteRenderSystem::class.java).update(deltaTime)
        engine.getSystem(ParticleRenderSystem::class.java).update(deltaTime)
        engine.getSystem(TextRenderSystem::class.java).update(deltaTime)
    }

    // your booty
    fun shake(shake: Shake) {
        if (shakeTime <= 0f) {
            defaultCameraPos.set(viewport.camera.position.x, viewport.camera.position.y)
            shakeTime = when (shake) {
                Shake.LARGE -> LARGE_SHAKE_DURATION
                Shake.SMALL -> SMALL_SHAKE_DURATION
                Shake.MASSIVE -> MASSIVE_SHAKE_DURATION
            }
            shakeIntensity = when (shake) {
                Shake.LARGE -> LARGE_SHAKE_INTENSITY
                Shake.SMALL -> SMALL_SHAKE_INTENSITY
                Shake.MASSIVE -> MASSIVE_SHAKE_INTENSITY
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        val abacus = engine.getSystem(AbacusSystem::class.java)
        when (keycode) {
            Input.Keys.M -> {
                Sounds.musicOn = !Sounds.musicOn
                return true
            }
            Input.Keys.N -> {
                Sounds.soundOn = !Sounds.soundOn
                return true
            }
            Input.Keys.SPACE -> {
                if (abacus.canShake()) {
                    shake(Shake.MASSIVE)
                    abacus.randomize()
                }

                return true
            }
            Input.Keys.W -> {
                abacus.moveUp(Player.ONE)
                abacus.print()
                return true
            }
            Input.Keys.S -> {
                abacus.moveDown(Player.ONE)
                abacus.print()
                return true
            }
            Input.Keys.D -> {
                if (!p1DoubleTap) {
                    p1DoubleTap = true
                    p1DoubleTime = DOUBLE_TAP_TIME
                } else {
                    p1DoubleTap = false
                    p1DoubleTime = DOUBLE_TAP_TIME
                    if (abacus.forceBead(Player.TWO)) {
                        shake(Shake.SMALL)
                    }
                }

                return true
            }
            Input.Keys.A -> {
                abacus.addBead(Player.ONE)
                abacus.print()
                return true
            }
            Input.Keys.UP -> {
                abacus.moveUp(Player.TWO)
                abacus.print()
                return true
            }
            Input.Keys.DOWN -> {
                abacus.moveDown(Player.TWO)
                abacus.print()
                return true
            }
            Input.Keys.RIGHT -> {
                abacus.addBead(Player.TWO)
                abacus.print()
                return true
            }
            Input.Keys.LEFT -> {
                if (!p2DoubleTap) {
                    p2DoubleTap = true
                    p2DoubleTime = DOUBLE_TAP_TIME
                } else {
                    p2DoubleTap = false
                    p2DoubleTime = DOUBLE_TAP_TIME
                    if (abacus.forceBead(Player.ONE)) {
                        shake(Shake.SMALL)
                    }
                }
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