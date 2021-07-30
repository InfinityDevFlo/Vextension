package eu.vironlab.vextension.console

import eu.vironlab.vextension.factory.Factory
import java.util.logging.Level
import java.util.logging.Logger

class DefaultConsole(override val logger: Logger = Logger.getGlobal()) : Console {
    constructor(): this(Logger.getGlobal())

    override fun echo(text: TextFactory.() -> Unit, level: Level) {
        logger.log(level, buildText(text).create())
    }

    override fun progressAnimation(animationType: ConsoleAnimationType): ConsoleAnimation {
        TODO("Not yet implemented")
    }
}

class UnixTextFactory(private val endLine: Boolean = true) : TextFactory, Factory<String> {
    constructor() : this(true)

    private val text: StringBuilder = StringBuilder()
    override fun append(text: String): UnixTextFactory {
        this.text.append(text)
        return this
    }

    override fun setColor(color: UnixColor): UnixTextFactory {
        this.text.append(color.text)
        return this
    }

    override fun setBackground(color: UnixColor): UnixTextFactory {
        this.text.append(color.background)
        return this
    }

    override fun create(): String {
        return text.toString() + if (endLine) "\n" else ""
    }

    override fun toString(): String = create()
}

enum class UnixColor(override val text: String, override val background: String): ConsoleColor {
    BLACK("\u001B[30m", "\u001B[40m"),
    RED("\u001B[31m", "\u001B[41m"),
    GREEN("\u001B[32m", "\u001B[42m"),
    YELLOW("\u001B[33m", "\u001B[43m"),
    BLUE("\u001B[34m", "\u001B[44m"),
    MAGENTA("\u001B[35m", "\u001B[45m"),
    CYAN("\u001B[36m", "\u001B[46m"),
    LIGHT_GRAY("\u001B[37m", "\u001B[47m"),
    DARK_GRAY("\u001B[90m", "\u001B[100m"),
    LIGHT_RED("\u001B[91m", "\u001B[101m"),
    LIGHT_GREEN("\u001B[92m", "\u001B[102m"),
    LIGHT_YELLOW("\u001B[93m", "\u001B[103m"),
    LIGHT_BLUE("\u001B[94m", "\u001B[104m"),
    LIGHT_MAGENTA("\u001B[95m", "\u001B[105m"),
    LIGHT_CYAN("\u001B[96m", "\u001B[106m"),
    WHITE("\u001B[97m", "\u001B[107m")
}

fun buildText(init: UnixTextFactory.() -> Unit): UnixTextFactory {
    val factory = UnixTextFactory()
    factory.init()
    return factory
}

fun echo(init: UnixTextFactory.() -> Unit) {
    print(buildText(init).create())
}