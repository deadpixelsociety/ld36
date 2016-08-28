package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils

// palette: http://www.colourlovers.com/palette/848743/(_%E2%80%9D_)
object Colors {
    val PARTY_CONFETTI = Color(73f / 255f, 10f / 255f, 61f / 255f, 1f)
    val SUGAR_HEARTS_YOU = Color(189f / 255f, 21f / 255f, 80f / 255f, 1f)
    val SUGAR_COCKTAIL = Color(233f / 255f, 127f / 255f, 2f / 255f, 1f)
    val BURSTS_OF_EUPHORIA = Color(248f / 255f, 202f / 255f, 0f, 1f)
    val HAPPY_BALLOON = Color(138f / 255f, 155f / 255f, 15f / 255f, 1f)

    fun random() = when (MathUtils.random(0, 2)) {
        0 -> SUGAR_HEARTS_YOU
        1 -> SUGAR_COCKTAIL
        2 -> BURSTS_OF_EUPHORIA
        else -> HAPPY_BALLOON
    }
}