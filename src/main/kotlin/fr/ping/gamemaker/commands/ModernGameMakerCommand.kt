package fr.ping.gamemaker.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.menus.MenuManager
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.entity.Player

object ModernGameMakerCommand {
  fun register() {
    val root : LiteralCommandNode<CommandSourceStack> = Commands.literal("gmk-modern")
      .then(Commands.literal("reload"))
      .then(Commands.literal("menu")
        .then(Commands.argument("menu", StringArgumentType.word())
          .suggests { ctx, builder ->
            GameMakerPlugin.menuTemplateRegistry.listIds()
              .filter { it.startsWith(builder.remainingLowerCase, ignoreCase = true) || it.contains(builder.remainingLowerCase, ignoreCase = true) }
              .forEach { builder.suggest(it) }
            builder.buildFuture()
          }
          .executes { ctx ->
            if (ctx.source.sender !is Player) {
              ctx.source.sender.sendRichMessage("<red>This command can only be executed by a player.")
              return@executes Command.SINGLE_SUCCESS
            }
            MenuManager.open(ctx.source.sender as Player, StringArgumentType.getString(ctx, "menu").split(" ").first())
            return@executes Command.SINGLE_SUCCESS
          }
        )
      )
      .build()

    GameMakerPlugin.getInstance().lifecycleManager.registerEventHandler(
      LifecycleEvents.COMMANDS
    ) { commands -> commands.registrar().register(root) }
  }
}