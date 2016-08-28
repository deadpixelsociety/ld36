package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.ParticleEffect

data class ParticleList(val effects: MutableList<ParticleEffect>) : Component