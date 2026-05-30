package fr.ping.gamemaker.notifications.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.notifications.models.ComposedNotification
import fr.ping.utils.resources.ResourceManager

class NotifyActionExecutor : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val notification = ResourceManager.parseAny<ComposedNotification>(action.data) ?: return
    notification.notify(context.player)
  }
}