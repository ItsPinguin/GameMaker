package fr.itspinguin.gamemaker.actions.impl

import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.menus.MenuManager

object ChangePageActionExecutor : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.MenuClickActionContext) return
    val list = action.data["list"]?.asString ?: return
    val pageOffset = action.data["page_offset"]?.asInt ?: return
    MenuManager.changePage(context.player, context.menu, list, pageOffset)
  }
}