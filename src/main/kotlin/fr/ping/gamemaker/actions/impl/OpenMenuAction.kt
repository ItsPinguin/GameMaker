package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.menus.MenuManager
import org.bukkit.entity.Player

object OpenMenuAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action != "open_menu") return
    val player = context["player"] as? Player ?: return
    val menu = action.data["menu"] as? String ?: return
    MenuManager.open(player, menu)
  }
}