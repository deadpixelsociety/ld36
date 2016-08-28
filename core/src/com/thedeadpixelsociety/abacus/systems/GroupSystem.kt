package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.thedeadpixelsociety.abacus.components.Group

class GroupSystem : ContainerSystem(Family.all(Group::class.java).get()) {
    private val groupMap = ObjectMap<String, Array<Entity>>()
    private val groupMapper = ComponentMapper.getFor(Group::class.java)

    operator fun get(name: String) = getEntitiesInGroup(name)

    override fun entityAdded(entity: Entity?) {
        entity?.let { groupMapper.get(it)?.let { getEntitiesInGroup(it.name).add(entity) } }
    }

    override fun entityRemoved(entity: Entity?) {
        entity?.let { groupMapper.get(it)?.let { getEntitiesInGroup(it.name).removeValue(entity, false) } }
    }

    private fun getEntitiesInGroup(group: String): Array<Entity> {
        var entities = groupMap.get(group)
        if (entities == null) {
            entities = Array()
            groupMap.put(group, entities)
        }

        return entities
    }
}