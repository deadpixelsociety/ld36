package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Disposable

object Fonts : Disposable {
    lateinit var huge: BitmapFont
        get
        private set

    lateinit var large: BitmapFont
        get
        private set

    lateinit var medium: BitmapFont
        get
        private set

    lateinit var smol: BitmapFont
        get
        private set

    fun create() {
        FreeTypeFontGenerator(Gdx.files.internal("fonts/BebasNeue.otf")).using {
            it.scaleForPixelHeight(144)
            huge = it.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 144
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.MipMapNearestLinear
            })
        }

        FreeTypeFontGenerator(Gdx.files.internal("fonts/BebasNeue.otf")).using {
            it.scaleForPixelHeight(72)
            large = it.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 72
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.MipMapNearestLinear
            })
        }

        FreeTypeFontGenerator(Gdx.files.internal("fonts/BebasNeue.otf")).using {
            it.scaleForPixelHeight(48)
            medium = it.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 48
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.MipMapNearestLinear
            })
        }

        FreeTypeFontGenerator(Gdx.files.internal("fonts/BebasNeue.otf")).using {
            it.scaleForPixelHeight(24)
            smol = it.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 24
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.MipMapNearestLinear
            })
        }
    }

    override fun dispose() {
        huge.dispose()
        large.dispose()
        medium.dispose()
        smol.dispose()
    }
}