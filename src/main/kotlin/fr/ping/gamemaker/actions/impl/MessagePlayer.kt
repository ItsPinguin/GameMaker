package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.i18n.I18nManager

object MessagePlayer : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    context.player.sendMessage(I18nManager[action.data["message"] as? String ?: "no_message"])
  }
}