package com.thedeadpixelsociety.abacus

import com.badlogic.gdx.utils.Disposable

fun <T : Disposable> T.using(func: (T) -> Unit) {
    try {
        func(this)
    } finally {
        dispose()
    }
}
