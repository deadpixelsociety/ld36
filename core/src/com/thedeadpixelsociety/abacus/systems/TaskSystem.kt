package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.thedeadpixelsociety.abacus.components.Task

class TaskSystem : IteratingSystem(Family.all(Task::class.java).get()) {
    private val taskMapper by lazy { ComponentMapper.getFor(Task::class.java) }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity?.let {
            val task = taskMapper.get(it)
            task?.let {
                if (task.func(entity, deltaTime)) {
                    entity.remove(Task::class.java)
                }
            }
        }
    }
}