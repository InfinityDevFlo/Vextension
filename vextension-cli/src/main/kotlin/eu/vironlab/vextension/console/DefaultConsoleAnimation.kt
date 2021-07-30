package eu.vironlab.vextension.console

import kotlinx.coroutines.delay
import java.lang.Thread.sleep
import java.util.logging.Level
import java.util.logging.Logger

class DefaultConsoleAnimation(
    override val type: ConsoleAnimationType,
    override val logger: Logger,
    override val level: Level
) : ConsoleAnimation {
    init {
        if (type.pitch < 10) throw Exception()
    }

    override var progress: Int = 0
    fun setProgress(progress: Int) = setProgress(progress, true)
    override fun setProgress(progress: Int, print: Boolean): ConsoleAnimation {
        if (progress !in 0..100) {
            throw IndexOutOfBoundsException("OUTOFRANGE")
        }
        val newProgress = (progress).floorDiv(type.pitch)
        if (print)
            print()
        return this
    }

    override fun print() {
        val space = " ".repeat(type.space / 2)
        logger.info(
            "\r${type.prefix}${space}" +
                    "${type.progressChar.toString().repeat(progress)}${space}${type.suffix}"
        )
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}

fun main() {
    DefaultConsoleAnimation(object : ConsoleAnimationType {
        override val prefix: String = "["
        override val suffix: String = "]"
        override val space: Int = 1
        override val progressChar: Char = '='
        override val pitch: Int = 10
    }, Logger.getLogger(DefaultConsoleAnimation::class.java.name), Level.ALL).let {
        for (i in 0..100 step 10) {
            it.setProgress(i)
            sleep(1000)
        }
    }
}