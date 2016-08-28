package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.IntMap
import com.thedeadpixelsociety.abacus.*
import com.thedeadpixelsociety.abacus.components.*
import com.thedeadpixelsociety.abacus.entities.Entities
import com.thedeadpixelsociety.abacus.world.AbacusState

class AbacusSystem(private val viewportWidth: Float, private val viewportHeight: Float) : EntitySystem() {
    companion object {
        const val ROW_START = 126f
        const val ROW_SPACING = 72f
        const val BEAD_START = 92f
        const val BEAD_SPACING = 8f
        const val BEAD_LEFT = 76f
        const val BEAD_RIGHT = 431f
        const val NUM_SHAKES = 2
        fun rowY(row: Int) = ROW_START + (row.toFloat() * ROW_SPACING)
    }

    private var state = AbacusState()
    private val beadMap = IntMap<Array<Entity>>()
    private val evenPattern = listOf(Entities.BeadType.RED, Entities.BeadType.ORANGE, Entities.BeadType.YELLOW)
    private val oddPattern = listOf(Entities.BeadType.YELLOW, Entities.BeadType.RED, Entities.BeadType.ORANGE)
    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private lateinit var arrowOne: Entity
    private lateinit var arrowTwo: Entity
    private lateinit var scoreOne: Entity
    private lateinit var scoreTwo: Entity
    private lateinit var target: Entity
    private lateinit var shakeText: Entity
    private lateinit var winnerText: Entity
    private val renderOffset = Vector2()
    private var shakes = NUM_SHAKES

    init {
        for (i in 0..AbacusState.NUM_ROWS - 1) {
            beadMap.put(i, Array<Entity>())
        }
    }

    fun canShake() = shakes > 0

    fun restart() {
        winnerText.getComponent(TextRender::class.java).visible = false
        shakes = NUM_SHAKES
        state = AbacusState()
        updateArrow(Player.ONE)
        updateArrow(Player.TWO)
        for (i in 0..AbacusState.NUM_ROWS - 1) updateRow(i)
        calculateScores()

        target.getComponent(TextRender::class.java).text = state.target.toString()
    }

    fun moveUp(player: Player) {
        state.moveUp(player)
        updateArrow(player)
    }

    fun moveDown(player: Player) {
        state.moveDown(player)
        updateArrow(player)
    }

    fun randomize() {
        shakes--
        if (shakes < 0) shakes = 0

        shakeText.getComponent(TextRender::class.java).text = shakes.toString()

        state.randomize()
        for (i in 0..AbacusState.NUM_ROWS - 1) updateRow(i)
        calculateScores()
    }

    fun dingPitch(player: Player) = .4f + (state.row(player).toFloat() * .1f)

    fun playDing(player: Player) = Sounds.playDing(dingPitch(player))

    fun forceBead(player: Player): Boolean {
        if (state.addBead(player, state.row(state.opponent(player)))) {
            playDing(player)

            // player is who we are forcing a bead onto so we're going to move the other arrow
            val arrow = when (player) {
                Player.ONE -> arrowTwo
                Player.TWO -> arrowOne
            }

            val punchTime = .3f
            val halfPunchTime = punchTime * .5f
            var punchTimer = 0f

            val beadCount = when (player) {
                Player.ONE -> state.beads(Player.ONE, state.row(Player.TWO))
                Player.TWO -> state.beads(Player.TWO, state.row(Player.ONE))
            }

            val transform = arrow.getComponent(Transform::class.java)
            val startPosition = transform.position.x
            val particleStart = renderOffset.x + when (player) {
                Player.ONE -> BEAD_LEFT + (((beadCount * Sprites.beadRed.width) + ((beadCount - 1) * BEAD_SPACING)) - Sprites.beadRed.width * .5f)
                Player.TWO -> BEAD_RIGHT - ((((beadCount - 1) * Sprites.beadRed.width) + ((beadCount - 1) * BEAD_SPACING)) - Sprites.beadRed.width * .5f)
            }

            val endPosition = startPosition + when (player) {
                Player.ONE -> 100f
                Player.TWO -> -100f
            }

            arrow.add(ParticleList(arrayListOf(Particles.hit().apply {
                setPosition(particleStart, renderOffset.y + transform.position.y + transform.height * .5f)
            })))

            arrow.add(Task({
                entity, deltaTime ->

                punchTimer += deltaTime
                entity.getComponent(Transform::class.java)?.let {
                    val step = if (punchTime >= halfPunchTime) {
                        Interpolation.bounceOut.apply(endPosition, startPosition, (punchTimer - halfPunchTime) / halfPunchTime)
                    } else {
                        Interpolation.bounceIn.apply(startPosition, endPosition, punchTimer / halfPunchTime)
                    }

                    it.position.x = step
                }

                punchTimer >= punchTime
            }))

            updateRow(state.row(state.opponent(player)))
            calculateScores()

            return true
        }

        return false
    }

    fun addBead(player: Player) {
        if (state.addBead(player)) {
            playDing(player)
        }

        updateRow(state.row(player))
        calculateScores()
    }

    fun removeBead(player: Player) {
        state.removeBead(player)
        playDing(player)

        updateRow(state.row(player))
        calculateScores()
    }

    fun print() = println(state)

    override fun addedToEngine(engine: Engine?) {
        engine?.let { createEntities(engine, it) }
        engine?.getSystem(TagSystem::class.java)?.get(Tags.STAND)?.let {
            val transform = transformMapper.get(it)
            renderOffset.set((viewportWidth - transform.width) * .5f, (viewportHeight - transform.height) * .5f)
        }
    }

    private fun createEntities(engine: Engine, it: Engine) {
        Entities.stand(it, 0f, 0f)

        scoreOne = Entities.score(engine, Player.ONE, "0")
        scoreTwo = Entities.score(engine, Player.TWO, "0")
        target = Entities.target(engine, state.target.toString())
        shakeText = Entities.shakes(engine, shakes.toString())
        winnerText = Entities.winner(engine, "ONE")

        for (i in 0..AbacusState.NUM_ROWS - 1) {
            val value = Math.pow(10.0, (AbacusState.NUM_ROWS - 1 - i).toDouble()).toLong()
            Entities.rowValue(engine, 16f, viewportHeight, Align.left, i, value.toString())
            Entities.rowValue(engine, viewportWidth - 16f, viewportHeight, Align.right, i, value.toString())
        }

        val beadsWidth = (Sprites.beadRed.width * AbacusState.NUM_BEADS) + (BEAD_SPACING * (AbacusState.NUM_BEADS - 1))
        val startX = (Sprites.stand.width - beadsWidth) * .5f
        var y = BEAD_START
        for (i in 0..AbacusState.NUM_ROWS - 1) {
            val pattern = if (i % 2 == 0) evenPattern else oddPattern
            var x = startX
            for (j in 0..AbacusState.NUM_BEADS - 1) {
                beadMap[i].add(Entities.bead(engine, x, y, pattern[j % 3]))
                x += Sprites.beadRed.width + BEAD_SPACING
            }

            y += ROW_SPACING
        }

        arrowOne = Entities.arrow(
            engine,
            -(Sprites.arrow.width),
            rowY(AbacusState.NUM_ROWS - 1) - (Sprites.arrow.height * .5f),
            Player.ONE
        )

        arrowTwo = Entities.arrow(
            engine,
            Sprites.stand.width,
            rowY(AbacusState.NUM_ROWS - 1) - (Sprites.arrow.height * .5f),
            Player.TWO
        )
    }

    private fun beads(row: Int) = beadMap[row] ?: Array()

    private fun updateArrow(player: Player) {
        val arrow = when (player) {
            Player.ONE -> arrowOne
            Player.TWO -> arrowTwo
        }

        transformMapper.get(arrow)?.let {
            it.position.set(it.position.x, rowY(state.row(player)) - Sprites.arrow.height * .5f)
        }
    }

    private fun updateRow(row: Int) {
        val beads = beads(row)
        val playerOneBeads = state.beads(Player.ONE, row)
        val availableBeads = state.availableBeads(row)
        val playerTwoBeads = state.beads(Player.TWO, row)

        val beadWidth = transformMapper.get(beads[0])?.width ?: 0f
        var x = BEAD_LEFT
        var beadIdx = 0

        if (playerOneBeads > 0) {
            for (i in 0..playerOneBeads - 1) {
                val bead = beads[beadIdx]
                transformMapper.get(bead)?.let {
                    it.position.set(x, it.position.y)
                }

                x += beadWidth + BEAD_SPACING
                beadIdx++
            }
        }

        if (availableBeads > 0) {
            x += beadWidth + BEAD_SPACING

            for (i in 0..availableBeads - 1) {
                val bead = beads[beadIdx]
                transformMapper.get(bead)?.let {
                    it.position.set(x, it.position.y)
                }

                x += beadWidth + BEAD_SPACING
                beadIdx++
            }
        } else {
            x = (BEAD_RIGHT - ((playerTwoBeads * beadWidth) + ((playerTwoBeads - 1) * BEAD_SPACING)))
        }


        if (playerTwoBeads > 0) {
            x += (transformMapper.get(beads[beadIdx])?.width ?: 0f) + BEAD_SPACING
            for (i in 0..playerTwoBeads - 1) {
                val bead = beads[beadIdx]
                transformMapper.get(bead)?.let {
                    it.position.set(x, it.position.y)
                    x += it.width + BEAD_SPACING
                }

                beadIdx++
            }
        }
    }

    private fun calculateScores() {
        scoreOne.getComponent(TextRender::class.java).text = state.calculate(Player.ONE).toString()
        scoreTwo.getComponent(TextRender::class.java).text = state.calculate(Player.TWO).toString()

        val winTime = 5f
        var winTimer = 0f
        when (state.checkWinner()) {
            Player.ONE -> {
                winnerText.getComponent(TextRender::class.java)?.let {
                    it.visible = true
                    it.text = "ONE"
                }

                winnerText.add(Task() {
                    entity, deltaTime ->
                    winTimer += deltaTime
                    if (winTimer >= winTime) {
                        restart()
                    }

                    winnerText.getComponent(Tint::class.java).color.set(Colors.random())

                    winTimer >= winTime
                })
            }
            Player.TWO -> {
                winnerText.getComponent(TextRender::class.java)?.let {
                    it.visible = true
                    it.text = "TWO"
                }

                winnerText.add(Task() {
                    entity, deltaTime ->
                    winTimer += deltaTime
                    if (winTimer >= winTime) {
                        restart()
                    }

                    winnerText.getComponent(Tint::class.java).color.set(Colors.random())

                    winTimer >= winTime
                })
            }
            null -> return
        }
    }
}