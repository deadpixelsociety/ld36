package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.utils.ObjectMap
import com.thedeadpixelsociety.abacus.components.Tag
import com.thedeadpixelsociety.abacus.systems.ContainerSystem

class TagSystem : ContainerSystem(Family.all(Tag::class.java).get()) {
    private val tagMap = ObjectMap<String, Entity>()
    private val tagMapper = ComponentMapper.getFor(Tag::class.java)

    operator fun get(name: String): Entity? = tagMap[name]

    override fun entityAdded(entity: Entity?) {
        entity?.let { tagMapper.get(it)?.let { tagMap.put(it.name, entity) } }
    }

    override fun entityRemoved(entity: Entity?) {
        entity?.let { tagMapper.get(it)?.let { tagMap.remove(it.name) } }
    }
}