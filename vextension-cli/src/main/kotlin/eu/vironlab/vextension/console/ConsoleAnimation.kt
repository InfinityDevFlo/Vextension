package eu.vironlab.vextension.console

import java.util.logging.Level
import java.util.logging.Logger

interface ConsoleAnimationType {
    val prefix: String
    val suffix: String
    val space: Int
    val progressChar: Char
    val pitch: Int
}
interface ConsoleAnimation {
    val type: ConsoleAnimationType
    val logger: Logger
    val level: Level
    var progress: Int
    fun setProgress(progress: Int, print: Boolean = true): ConsoleAnimation
    fun print()
    fun stop()
}