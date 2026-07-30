package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.menus.MenuManager

object OpenMenuAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val menu = action.data["menu"]?.asString ?: return
    MenuManager.open(context.player, menu, action.data.asMap())
  }
}