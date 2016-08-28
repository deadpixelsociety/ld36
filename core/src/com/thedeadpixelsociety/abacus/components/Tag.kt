package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component

/**
 * A component used to identify an entity by a unique name (e.g., "PLAYER").
 * @param name The unique name of the entity.
 */
data class Tag(val name: String) : Component