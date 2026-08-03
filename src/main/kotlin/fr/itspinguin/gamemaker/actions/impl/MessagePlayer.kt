package fr.itspinguin.gamemaker.actions.impl

import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.i18n.I18nManager

object MessagePlayer : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    context.player.sendMessage(I18nManager[action.data["message"]?.asString ?: "no_message"])
  }
}