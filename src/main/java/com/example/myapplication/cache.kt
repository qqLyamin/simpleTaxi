package com.example.myapplication

import android.graphics.Bitmap
import java.util.concurrent.TimeUnit

interface Cache {
    val size: Int

    operator fun set(key: String, value: Bitmap)

    operator fun get(key: String): Bitmap?

    fun remove(key: String): Bitmap?

    fun clear()
}

class PerpetualCache : Cache {
    private val cache = HashMap<String, Bitmap>()

    override val size: Int
        get() = cache.size

    override fun set(key: String, value: Bitmap) {
        this.cache[key] = value
    }

    override fun remove(key: String) = this.cache.remove(key)

    override fun get(key: String) = this.cache[key]

    override fun clear() = this.cache.clear()
}

class ExpirableCache(private val delegate: Cache,
                     private val flushInterval: Long = TimeUnit.MINUTES.toMillis(10)) : Cache {
    private var lastFlushTime = System.nanoTime()

    override val size: Int
        get() = delegate.size

    override fun set(key: String, value: Bitmap) {
        delegate[key] = value
    }

    override fun remove(key: String): Bitmap? {
        recycle()
        return delegate.remove(key)
    }

    override fun get(key: String): Bitmap? {
        recycle()
        return delegate[key]
    }

    override fun clear() = delegate.clear()

    private fun recycle() {
        val shouldRecycle = System.nanoTime() - lastFlushTime >= TimeUnit.MILLISECONDS.toNanos(flushInterval)
        if (!shouldRecycle) return
        delegate.clear()
    }
}
