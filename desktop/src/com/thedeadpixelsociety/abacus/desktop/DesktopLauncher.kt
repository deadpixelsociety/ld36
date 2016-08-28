package com.thedeadpixelsociety.abacus.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.utils.GdxNativesLoader
import com.thedeadpixelsociety.abacus.AbacusGame

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        GdxNativesLoader.load()
        LwjglApplication(AbacusGame(), LwjglApplicationConfiguration().apply {
            title = "Abacus Fighter (Ludum Dare 36) :: @deadpxlsociety"
            width = 1280
            height = 720
            addIcon("icon128.png", Files.FileType.Internal)
            addIcon("icon32.png", Files.FileType.Internal)
            addIcon("icon16.png", Files.FileType.Internal)
        })
    }
}
