package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.thedeadpixelsociety.abacus.Tags
import com.thedeadpixelsociety.abacus.components.TextRender
import com.thedeadpixelsociety.abacus.components.Tint
import com.thedeadpixelsociety.abacus.components.Transform
import com.thedeadpixelsociety.abacus.services.service

class TextRenderSystem(
    private val viewport: Viewport,
    private val viewportWidth: Float,
    private val viewportHeight: Float
) : IteratingSystem(Family.all(Transform::class.java, TextRender::class.java).get()) {

    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private val tintMapper by lazy { ComponentMapper.getFor(Tint::class.java) }
    private val textRenderMapper by lazy { ComponentMapper.getFor(TextRender::class.java) }
    private val defaultTint by lazy { Tint() }
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
            val textRender = textRenderMapper.get(it)
            val tint = tintMapper.get(it) ?: defaultTint

            if (transform != null && textRender != null && textRender.visible) {
                renderText(transform, textRender, tint)
            }
        }
    }

    private fun renderText(transform: Transform, textRender: TextRender, tint: Tint) {
        textRender.text?.let {
            text ->
            textRender.font.apply {
                color.set(tint.color)

                val formatted = textRender.format?.invoke(text) ?: text
                val p = Vector2()
                val position = textRender.position?.invoke(formatted, viewportWidth, viewportHeight)
                    ?: p.set(renderOffset).add(transform.position)

                draw(spriteBatch, formatted, position.x, position.y, 0f, textRender.align, false)
            }
        }
    }

    private fun end() {
        spriteBatch.end()
    }
}