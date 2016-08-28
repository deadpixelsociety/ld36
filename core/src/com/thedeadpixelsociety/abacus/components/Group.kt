package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component

/**
 * A component used to qualify an entity as belonging to a certain group of entities (e.g., "ENEMIES").
 * @param name The group name.
 */
data class Group(val name: String) : Component