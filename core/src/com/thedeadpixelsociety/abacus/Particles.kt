package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.utils.Disposable

object Particles : Disposable {
    private lateinit var hit: ParticleEffectPool

    fun create() {
        hit = ParticleEffectPool(ParticleEffect().apply {
            load(Gdx.files.internal("particles/hit.px"), Gdx.files.internal("particles"))
        }, 10, 20)
    }

    fun hit() = hit.obtain()
    fun freeHit(effect: ParticleEffectPool.PooledEffect) = hit.free(effect)

    override fun dispose() {
        hit.clear()
    }
}