package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.thedeadpixelsociety.abacus.Particles
import com.thedeadpixelsociety.abacus.Tags
import com.thedeadpixelsociety.abacus.components.ParticleList
import com.thedeadpixelsociety.abacus.components.Transform
import com.thedeadpixelsociety.abacus.services.service

class ParticleRenderSystem(
    private val viewport: Viewport,
    private val viewportWidth: Float,
    private val viewportHeight: Float
) : IteratingSystem(Family.all(Transform::class.java, ParticleList::class.java).get()) {

    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private val particleMapper by lazy { ComponentMapper.getFor(ParticleList::class.java) }
    private val spriteBatch by service(SpriteBatch::class)
    private val renderOffset = Vector2()

    // will update manually during the draw phase
    override fun checkProcessing() = false

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine?.getSystem(TagSystem::class.java)?.get(Tags.STAND)?.let {
            val transform = transformMapper.get(it)
            renderOffset.set((viewportWidth - transform.width) * .5f, (viewportHeight - transform.height) * .5f)
        }
    }

    override fun update(deltaTime: Float) {
        begin()
        super.update(deltaTime)
        end()
    }

    private fun begin() {
        viewport.apply()
        spriteBatch.projectionMatrix = viewport.camera.combined
        spriteBatch.begin()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            val transform = transformMapper.get(it)
            val particles = particleMapper.get(it)

            if (transform != null && particles != null) {
                renderParticles(deltaTime, entity, particles)
            }
        }
    }

    private fun renderParticles(deltaTime: Float, entity: Entity, particles: ParticleList) {
        val finished = arrayListOf<ParticleEffect>()
        particles.effects.forEach {
            it.draw(spriteBatch, deltaTime)
            if (it.isComplete) {
                if (it is ParticleEffectPool.PooledEffect) Particles.freeHit(it)
                finished.add(it)
            }
        }

        finished.forEach { particles.effects.remove(it) }
        if (particles.effects.size == 0) {
            entity.remove(ParticleList::class.java)
        }
    }

    private fun end() {
        spriteBatch.end()
    }
}