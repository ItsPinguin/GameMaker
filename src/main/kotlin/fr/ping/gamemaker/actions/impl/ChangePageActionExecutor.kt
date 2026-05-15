package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.menus.MenuManager
import fr.ping.gamemaker.menus.models.MenuInstance
import org.bukkit.entity.Player

object ChangePageActionExecutor : ActionExecutor() {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action != "change_page") return
    val player = context["player"] as? Player ?: return
    val menuInstance = context["menu_instance"] as? MenuInstance ?: return
    val list = action.data["list"] as? String ?: return
    val pageOffset = action.data["page_offset"].toString().toDoubleOrNull()?.toInt() ?: return
    MenuManager.changePage(player, menuInstance, list, pageOffset)
  }
}