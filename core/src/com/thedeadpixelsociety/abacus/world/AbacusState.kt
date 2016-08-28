package com.thedeadpixelsociety.abacus.world

import com.badlogic.gdx.math.MathUtils
import com.thedeadpixelsociety.abacus.Player

class AbacusState {
    companion object {
        const val NUM_ROWS = 6
        const val NUM_BEADS = 10
        const val MAX_VALUE = 1111110L
    }

    private val playerBeads = hashMapOf<Player, MutableList<Int>>()
    private val freeBeads = arrayListOf<Int>()
    private var playerRow = hashMapOf<Player, Int>()
    val target = MathUtils.random(1L, MAX_VALUE)

    init {
        resetBeads()

        playerRow[Player.ONE] = NUM_ROWS - 1
        playerRow[Player.TWO] = NUM_ROWS - 1
    }

    private fun resetBeads() {
        freeBeads.clear()
        val playerOne = arrayListOf<Int>()
        val playerTwo = arrayListOf<Int>()
        for (i in 0..NUM_ROWS - 1) {
            playerOne.add(0)
            playerTwo.add(0)
            freeBeads.add(NUM_BEADS)
        }

        playerBeads[Player.ONE] = playerOne
        playerBeads[Player.TWO] = playerTwo
    }

    fun checkWinner() = if (calculate(Player.ONE) == target) Player.ONE
    else if (calculate(Player.TWO) == target) Player.TWO
    else null

    fun randomize() {
        resetBeads()

        for (i in 0..NUM_ROWS - 1) {
            for (j in 0..NUM_BEADS - 1) {
                val chance = MathUtils.random()
                if (chance <= .33f) {
                    val beads = beads(Player.ONE, i)
                    playerBeads[Player.ONE]?.set(i, beads + 1)
                    freeBeads[i] = freeBeads[i] - 1
                } else if (chance <= .66f) {
                    val beads = beads(Player.TWO, i)
                    playerBeads[Player.TWO]?.set(i, beads + 1)
                    freeBeads[i] = freeBeads[i] - 1
                }
            }
        }
    }

    fun calculate(player: Player): Long {
        var total = 0L
        for (i in NUM_ROWS - 1 downTo 0) {
            val beads = beads(player, i)
            val value = Math.pow(10.0, (NUM_ROWS - 1 - i).toDouble()).toLong()
            total += (value * beads)
        }

        return total
    }

    fun row(player: Player) = playerRow[player] ?: 0

    fun moveUp(player: Player) {
        val row = row(player) + 1
        playerRow[player] = if (row >= NUM_ROWS) 0 else row
    }

    fun moveDown(player: Player) {
        val row = row(player) - 1
        playerRow[player] = if (row < 0) NUM_ROWS - 1 else row
    }

    fun beads(player: Player, row: Int) = playerBeads[player]?.get(row) ?: 0

    fun availableBeads(row: Int) = freeBeads[row]

    fun addBead(player: Player) = addBead(player, row(player))

    fun addBead(player: Player, row: Int): Boolean {
        val beads = beads(player, row)
        val availableBeads = freeBeads[row]
        if (availableBeads > 0) {
            freeBeads[row] = availableBeads - 1
            playerBeads[player]?.set(row, beads + 1)
            return true
        }

        val opponentBeads = beads(opponent(player), row)
        if (opponentBeads > 0) {
            playerBeads[opponent(player)]?.set(row, opponentBeads - 1)
            playerBeads[player]?.set(row, beads + 1)
            return true
        }

        return false
    }

    fun removeBead(player: Player): Boolean {
        val row = row(player)
        val beads = beads(player, row)
        if (beads == 0) {
            val availableBeads = freeBeads[row]
            if (availableBeads > 0) {
                return addBead(opponent(player), row)
            }

            return false
        }

        freeBeads[row] = freeBeads[row] + 1
        playerBeads[player]?.set(row, beads - 1)
        return false
    }

    fun opponent(player: Player) = when (player) {
        Player.ONE -> Player.TWO
        Player.TWO -> Player.ONE
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for (i in NUM_ROWS - 1 downTo 0) {
            sb.append(if (row(Player.ONE) == i) '>' else ' ')
            for (j in 0..beads(Player.ONE, i) - 1) sb.append('@')
            sb.append('.')
            for (j in 0..freeBeads[i] - 1) sb.append('@')
            sb.append('.')
            for (j in 0..beads(Player.TWO, i) - 1) sb.append('@')
            sb.append(if (row(Player.TWO) == i) '<' else ' ')
            sb.appendln()
        }

        sb.appendln("one: ${calculate(Player.ONE)}")
        sb.appendln("two: ${calculate(Player.TWO)}")

        return sb.toString()
    }
}
