package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.menus.MenuManager

object ChangePageActionExecutor : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.MenuClickActionContext) return
    val list = action.data["list"] as? String ?: return
    val pageOffset = action.data["page_offset"].toString().toDoubleOrNull()?.toInt() ?: return
    MenuManager.changePage(context.player, context.menu, list, pageOffset)
  }
}