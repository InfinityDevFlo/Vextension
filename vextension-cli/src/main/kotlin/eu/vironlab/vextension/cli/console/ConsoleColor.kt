package eu.vironlab.vextension.cli.console

import eu.vironlab.vextension.lang.Nameable
import org.fusesource.jansi.Ansi;

class ConsoleColor(override val name: String, val index: Char, val ansi: String) : Nameable {

    companion object {
        @JvmStatic
        val DEFAULT: ConsoleColor = ConsoleColor(
            "default",
            'r',
            Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT).boldOff().toString()
        )
        @JvmStatic
        val BLACK: ConsoleColor =
            ConsoleColor("black", '0', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString())
        @JvmStatic
        val DARK_BLUE: ConsoleColor =
            ConsoleColor("dark_blue", '1', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString())
        @JvmStatic
        val GREEN: ConsoleColor =
            ConsoleColor("green", '2', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString())
        @JvmStatic
        val CYAN: ConsoleColor =
            ConsoleColor("cyan", '3', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString())
        @JvmStatic
        val DARK_RED: ConsoleColor =
            ConsoleColor("dark_red", '4', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString())
        @JvmStatic
        val PURPLE: ConsoleColor =
            ConsoleColor("purple", '5', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString())
        @JvmStatic
        val ORANGE: ConsoleColor = ConsoleColor(
            "orange",
            '6',
            Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).fg(Ansi.Color.YELLOW).boldOff().toString()
        )
        @JvmStatic
        val GRAY: ConsoleColor =
            ConsoleColor("gray", '7', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString())
        @JvmStatic
        val DARK_GRAY: ConsoleColor =
            ConsoleColor("dark_gray", '8', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString())
        @JvmStatic
        val BLUE: ConsoleColor =
            ConsoleColor("blue", '9', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString())
        @JvmStatic
        val LIGHT_GREEN: ConsoleColor =
            ConsoleColor("light_green", 'a', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString())
        @JvmStatic
        val AQUA: ConsoleColor =
            ConsoleColor("aqua", 'b', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString())
        @JvmStatic
        val RED: ConsoleColor =
            ConsoleColor("red", 'c', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString())
        @JvmStatic
        val PINK: ConsoleColor =
            ConsoleColor("pink", 'd', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString())
        @JvmStatic
        val YELLOW: ConsoleColor =
            ConsoleColor("yellow", 'e', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString())
        @JvmStatic
        val WHITE: ConsoleColor =
            ConsoleColor("white", 'f', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString())
    }


}
