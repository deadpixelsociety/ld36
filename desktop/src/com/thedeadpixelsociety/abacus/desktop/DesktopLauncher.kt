package com.thedeadpixelsociety.abacus.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.thedeadpixelsociety.abacus.AbacusGame

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        LwjglApplication(AbacusGame(), LwjglApplicationConfiguration().apply {
            title = "Abacus Fighter"
        })
    }
}
