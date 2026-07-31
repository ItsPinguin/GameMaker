package fr.itspinguin.gamemaker.actions.impl

import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.menus.MenuManager

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