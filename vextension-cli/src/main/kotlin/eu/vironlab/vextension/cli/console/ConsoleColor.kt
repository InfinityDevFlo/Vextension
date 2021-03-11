package eu.vironlab.vextension.cli.console

import eu.vironlab.vextension.cli.logging.Logger
import eu.vironlab.vextension.lang.Nameable
import org.fusesource.jansi.Ansi;

enum class ConsoleColor(val colorName: String, val index: Char, val ansi: String) {
    BLACK("black", '0', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString()),
    DARK_BLUE("dark_blue", '1', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString()),
    GREEN("green", '2', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString()),
    CYAN("cyan", '3', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString()),
    DARK_RED("dark_red", '4', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString()),
    PURPLE("purple", '5', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString()),
    ORANGE("orange", '6', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).fg(Ansi.Color.YELLOW).boldOff().toString()),
    GRAY("gray", '7', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString()),
    DARK_GRAY("dark_gray", '8', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
    BLUE("blue", '9', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
    LIGHT_GREEN("light_green", 'a', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
    AQUA("aqua", 'b', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
    RED("red", 'c', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
    PINK("pink", 'd', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString()),
    YELLOW("yellow", 'e', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
    WHITE("white", 'f', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());

    companion object {

        @JvmStatic
        fun render(triggerChar: Char, input: String): String {
            var output: String = input
            for (color in values()) {
                output = input.replace("${triggerChar}${color.index}", color.ansi)
            }
            return output
        }
    }
}
