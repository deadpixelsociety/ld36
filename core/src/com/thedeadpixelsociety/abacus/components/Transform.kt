package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

/**
 * Provides transformation info about an entity. This transformation data includes the height and width of the entity
 * which arguably may belong in another component. However the width and height of a thing is usually used along with
 * it's position and origin so it makes sense to normalize it here for ease of access.
 * @param position The world position as a [Vector2].
 * @param origin The local origin (relative to the position) as a [Vector2].
 * @param scale The x/y scale as a [Vector2].
 * @param rotation The rotational angle (in degrees).
 */
data class Transform(
    val position: Vector2 = Vector2(0f, 0f),
    val origin: Vector2 = Vector2(0f, 0f),
    val scale: Vector2 = Vector2(1f, 1f),
    var rotation: Float = 0f,
    var width: Float = 0f,
    var height: Float = 0f
) : Component {
    fun halfWidth() = width * .5f
    fun halfHeight() = height * .5f
}