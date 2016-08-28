package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color

/**
 * Defines the tint color of an entity. Defaults to solid white (e.g., [Color#WHITE]).
 * @param color The [Color] tint of the entity.
 */
data class Tint(val color: Color = Color(Color.WHITE)) : Component