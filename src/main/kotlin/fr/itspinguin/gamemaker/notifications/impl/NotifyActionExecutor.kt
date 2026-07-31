package fr.itspinguin.gamemaker.notifications.impl

import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.notifications.models.ComposedNotification
import fr.itspinguin.resourcemanager.ResourceManager
import fr.itspinguin.resourcemanager.WrappedResource

class NotifyActionExecutor : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    if (context !is ActionContext.PlayerActionContext) return
    val notification = ResourceManager.parseAny<WrappedResource<ComposedNotification>>(action.data["notification"]) ?: return
    notification.get()?.notify(context.player)
  }
}