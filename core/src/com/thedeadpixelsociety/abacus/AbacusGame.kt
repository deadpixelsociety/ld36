package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.thedeadpixelsociety.abacus.logging.*
import com.thedeadpixelsociety.abacus.services.disposeServices
import com.thedeadpixelsociety.abacus.services.registerService
import com.thedeadpixelsociety.abacus.states.GameStateStack
import com.thedeadpixelsociety.abacus.states.MenuState

class AbacusGame : ApplicationAdapter() {
    companion object {
        const val DT = .01f
        const val MAX_DT = .16f
    }

    private val states by lazy { GameStateStack() }
    private var accumulator = 0f
    private var totalTime = 0f

    override fun create() {
        logLevel(LogLevel.TRACE)
        logTargets(PrintTarget())

        Gdx.graphics.setWindowedMode(
            (Gdx.graphics.displayMode.width * .66f).toInt(),
            (Gdx.graphics.displayMode.height * .66f).toInt()
        )

        registerService(SpriteBatch())
        registerService(ShapeRenderer())
        registerService(states)

        Fonts.create()
        Sprites.create()
        Sounds.create()
        Particles.create()

        states.add(MenuState())
    }

    override fun render() {
        Gdx.gl.glClearColor(Colors.PARTY_CONFETTI.r, Colors.PARTY_CONFETTI.g, Colors.PARTY_CONFETTI.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        accumulator += Math.min(MAX_DT, Gdx.graphics.deltaTime)
        while (accumulator >= DT) {
            states.update(totalTime, DT)
            totalTime += DT
            accumulator -= DT
        }

        states.draw(totalTime, DT)
    }

    override fun pause() {
        states.pause()
    }

    override fun resize(width: Int, height: Int) {
        states.resize(width, height)
    }

    override fun resume() {
        states.resume()
    }

    override fun dispose() {
        disposeLogger()
        disposeServices()
        Fonts.dispose()
        Sprites.dispose()
        Sounds.dispose()
        Particles.dispose()
    }
}
