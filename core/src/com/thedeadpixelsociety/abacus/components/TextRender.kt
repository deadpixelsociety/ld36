package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align

data class TextRender(
    val font: BitmapFont,
    var text: String? = null,
    val format: ((String) -> String)? = null,
    val position: ((String, Float, Float) -> Vector2)? = null,
    val align: Int = Align.left,
    var visible: Boolean = true
) : Component