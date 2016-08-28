package com.thedeadpixelsociety.abacus.systems

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array

/**
 * An implementation of [EntitySystem] that collects entities belonging to it's [family] but does not process them.
 * @param family The [Family] the defines the entities in this system.
 */
open class ContainerSystem(val family: Family, priority: Int = 0) : EntitySystem(priority), EntityListener {
    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        entities = engine?.getEntitiesFor(family) ?: ImmutableArray(Array())
        engine?.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        engine?.removeEntityListener(this)
        super.removedFromEngine(engine)
    }

    override fun entityAdded(entity: Entity?) {
    }

    override fun entityRemoved(entity: Entity?) {
    }
}