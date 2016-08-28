package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Disposable

object Sounds : Disposable {
    lateinit var ding: Sound
        get
        private set
    lateinit var music: Music
        get
        private set

    var soundOn = true

    var musicOn = true
        get
        set(value) {
            if (value) playMusic() else stopMusic()
            field = value
        }

    fun playDing(pitch: Float = 1f) {
        if (soundOn) {
            ding.play(1f, pitch, 0f)
        }
    }

    fun playMusic() {
        if (!music.isPlaying) {
            music.volume = .5f
            music.isLooping = true
            music.play()
        }
    }

    fun stopMusic() {
        if (music.isPlaying) music.stop()
    }

    fun create() {
        ding = Gdx.audio.newSound(Gdx.files.local("sounds/ding.wav"))
        music = Gdx.audio.newMusic(Gdx.files.local("sounds/song.wav"))
    }

    override fun dispose() {
        ding.dispose()
        music.dispose()
    }
}