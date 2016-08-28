package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.thedeadpixelsociety.abacus.Colors
import com.thedeadpixelsociety.abacus.Tags
import com.thedeadpixelsociety.abacus.components.SpriteRender
import com.thedeadpixelsociety.abacus.components.Tint
import com.thedeadpixelsociety.abacus.components.Transform
import com.thedeadpixelsociety.abacus.services.service
import com.thedeadpixelsociety.abacus.world.AbacusState

class SpriteRenderSystem(
    private val viewport: Viewport,
    private val viewportWidth: Float,
    private val viewportHeight: Float
) : IteratingSystem(Family.all(Transform::class.java, SpriteRender::class.java).get()) {

    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private val tintMapper by lazy { ComponentMapper.getFor(Tint::class.java) }
    private val spriteRenderMapper by lazy { ComponentMapper.getFor(SpriteRender::class.java) }
    private val defaultTint by lazy { Tint() }
    private val spriteBatch by service(SpriteBatch::class)
    private val shapeRenderer by service(ShapeRenderer::class)
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
        drawLines()

        begin()
        super.update(deltaTime)
        end()
    }

    private fun drawLines() {
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.color.set(Colors.HAPPY_BALLOON)
        shapeRenderer.color.a = .3f
        for (i in 0..AbacusState.NUM_ROWS - 1) {
            shapeRenderer.line(0f, renderOffset.y + AbacusSystem.rowY(i), viewportWidth, renderOffset.y + AbacusSystem.rowY(i))
        }

        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    private fun begin() {
        viewport.apply()
        spriteBatch.projectionMatrix = viewport.camera.combined
        spriteBatch.begin()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            val transform = transformMapper.get(it)
            val spriteRender = spriteRenderMapper.get(it)
            val tint = tintMapper.get(it) ?: defaultTint

            if (transform != null && spriteRender != null) {
                renderSprite(transform, spriteRender, tint)
            }
        }
    }

    private fun renderSprite(transform: Transform, spriteRender: SpriteRender, tint: Tint) {
        spriteRender.sprite.apply {
            color.set(tint.color)
            rotation = transform.rotation
            setSize(transform.width, transform.height)
            setScale(transform.scale.x, transform.scale.y)
            setOrigin(transform.origin.x, transform.origin.y)
            setPosition(renderOffset.x + transform.position.x, renderOffset.y + transform.position.y)

            draw(spriteBatch)
        }
    }

    private fun end() {
        spriteBatch.end()
    }
}