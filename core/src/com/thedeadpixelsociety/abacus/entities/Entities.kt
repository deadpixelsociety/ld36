package com.thedeadpixelsociety.abacus.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector2
import com.thedeadpixelsociety.abacus.*
import com.thedeadpixelsociety.abacus.components.*
import com.thedeadpixelsociety.abacus.systems.AbacusSystem
import com.thedeadpixelsociety.abacus.systems.TagSystem
import java.text.MessageFormat

object Entities {
    enum class BeadType {
        RED,
        ORANGE,
        YELLOW
    }

    private val layout = GlyphLayout()

    fun stand(engine: Engine, x: Float, y: Float): Entity {
        val entity = engine.createEntity()

        entity.add(Transform().apply {
            position.set(x, y)
            width = Sprites.stand.width
            height = Sprites.stand.width
        })

        entity.add(SpriteRender(Sprites.stand))
        entity.add(Tag(Tags.STAND))

        engine.addEntity(entity)

        return entity
    }

    fun bead(engine: Engine, x: Float, y: Float, type: BeadType): Entity {
        val entity = engine.createEntity()

        entity.add(Transform().apply {
            position.set(x, y)
            width = Sprites.beadRed.width
            height = Sprites.beadRed.height
            origin.set(width * .5f, height * .5f)
        })

        entity.add(SpriteRender(when (type) {
            Entities.BeadType.RED -> Sprites.beadRed
            Entities.BeadType.ORANGE -> Sprites.beadOrange
            Entities.BeadType.YELLOW -> Sprites.beadYellow
        }))

        entity.add(Tint())
        entity.add(Group(Groups.BEADS))

        engine.addEntity(entity)

        return entity
    }

    fun target(engine: Engine, text: String? = null): Entity {
        val entity = engine.createEntity()

        entity.add(Transform())

        entity.add(TextRender(Fonts.large, text, { MessageFormat.format("CALCULATE {0}", it.toInt()) }, {
            text, width, height ->
            layout.setText(Fonts.large, text)
            Vector2().apply {
                this.x = (width - layout.width) * .5f
                this.y = height - layout.height - 8f
            }
        }))

        entity.add(Tint())
        entity.add(Tag(Tags.TARGET))

        engine.addEntity(entity)

        return entity
    }

    fun rowValue(engine: Engine, x: Float, viewHeight: Float, align: Int, row: Int, text: String? = null): Entity {
        val entity = engine.createEntity()

        entity.add(Transform())

        entity.add(TextRender(Fonts.medium, text, { MessageFormat.format("{0}", it.toInt()) }, {
            text, width, height ->
            layout.setText(Fonts.medium, text)
            Vector2().apply {
                engine.getSystem(TagSystem::class.java)?.get(Tags.STAND)?.let {
                    val transform = it.getComponent(Transform::class.java)
                    this.y = ((viewHeight - transform.height) * .5f) + AbacusSystem.rowY(row) + (layout.height * .5f)
                }

                this.x = x
            }
        }, align))

        entity.add(Tint())

        engine.addEntity(entity)

        return entity
    }

    fun score(engine: Engine, player: Player, text: String? = null): Entity {
        val entity = engine.createEntity()

        entity.add(Transform())

        entity.add(TextRender(Fonts.medium, text, { MessageFormat.format("PLAYER {0} {1}", player.name, it.toInt()) }, {
            text, width, height ->
            layout.setText(Fonts.medium, text)
            Vector2().apply {
                this.x = (width - layout.width) * .5f
                this.y = height - layout.height - when (player) {
                    Player.ONE -> 100f
                    Player.TWO -> 148f
                }
            }
        }))
        entity.add(Tint())
        entity.add(Tag(when (player) {
            Player.ONE -> Tags.SCORE_ONE
            Player.TWO -> Tags.SCORE_TWO
        }))

        engine.addEntity(entity)

        return entity
    }

    fun winner(engine: Engine, text: String? = null): Entity {
        val entity = engine.createEntity()

        entity.add(Transform())

        entity.add(TextRender(Fonts.huge, text, { MessageFormat.format("PLAYER {0} WINS", it) }, {
            text, width, height ->
            layout.setText(Fonts.huge, text)
            Vector2().apply {
                this.x = (width - layout.width) * .5f
                this.y = (height - (layout.height * 2))
            }
        }, visible = false))

        entity.add(Tint())

        engine.addEntity(entity)

        return entity
    }

    fun shakes(engine: Engine, text: String? = null): Entity {
        val entity = engine.createEntity()

        entity.add(Transform())

        entity.add(TextRender(Fonts.large, text, { MessageFormat.format("SHAKES LEFT x{0}", it.toInt()) }, {
            text, width, height ->
            layout.setText(Fonts.large, text)
            Vector2().apply {
                this.x = (width - layout.width) * .5f
                this.y = layout.height + 32f
            }
        }))

        entity.add(Tint())

        engine.addEntity(entity)

        return entity
    }

    fun arrow(engine: Engine, x: Float, y: Float, player: Player): Entity {
        val entity = engine.createEntity()

        entity.add(Transform().apply {
            position.set(x, y)
            width = Sprites.arrow.width
            height = Sprites.arrow.height
            scale.set(when (player) {
                Player.ONE -> 1f
                Player.TWO -> -1f
            }, 1f)
            origin.set(width * .5f, height * .5f)
        })

        entity.add(SpriteRender(Sprites.arrow))

        entity.add(Tint())
        entity.add(Tag(when (player) {
            Player.ONE -> Tags.ARROW_ONE
            Player.TWO -> Tags.ARROW_TWO
        }))

        engine.addEntity(entity)

        return entity
    }
}