package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import org.bukkit.entity.Player

object CommandAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action != "command") return
    val player = context["player"] as? Player ?: return
    player.performCommand(action.data["command"] as? String ?: return)
  }
}