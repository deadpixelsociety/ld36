package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

object Sprites : Disposable {
    lateinit var beadRed: Sprite
        get
        private set

    lateinit var beadOrange: Sprite
        get
        private set

    lateinit var beadYellow: Sprite
        get
        private set

    lateinit var stand: Sprite
        get
        private set

    lateinit var arrow: Sprite
        get
        private set

    lateinit var n1: Sprite
        get
        private set

    lateinit var n10: Sprite
        get
        private set

    lateinit var n100: Sprite
        get
        private set

    lateinit var n1000: Sprite
        get
        private set

    lateinit var n10000: Sprite
        get
        private set

    lateinit var n100000: Sprite
        get
        private set
    lateinit var title: Sprite
        get
        private set

    private val pack by lazy { TextureAtlas(Gdx.files.internal("packs/abacus.pack")) }

    fun create() {
        beadRed = pack.createSprite("bead_red")
        beadOrange = pack.createSprite("bead_orange")
        beadYellow = pack.createSprite("bead_yellow")
        stand = pack.createSprite("stand")
        arrow = pack.createSprite("arrow")
        n1 = pack.createSprite("1")
        n10 = pack.createSprite("10")
        n100 = pack.createSprite("100")
        n1000 = pack.createSprite("1000")
        n10000 = pack.createSprite("10000")
        n100000 = pack.createSprite("100000")
        title = pack.createSprite("title")
    }

    override fun dispose() {
        pack.dispose()
    }
}