package com.thedeadpixelsociety.abacus.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

data class Task(val func: (Entity, Float) -> Boolean) : Component