package eu.vironlab.vextension.command.executor

import eu.vironlab.vextension.command.context.CommandContext
import eu.vironlab.vextension.command.source.CommandSource

interface CommandExecutor<S : CommandSource, C : CommandContext<S>>
