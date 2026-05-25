package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionContext
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.dialog.Dialog
import fr.ping.utils.resources.ResourceManager

object DialogAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: ActionContext
  ) {
    val dialogId = action.data["dialog"] as? String ?: return
    @Suppress("DEPRECATION")
    val dialog = ResourceManager[dialogId, Dialog::class.java] ?: return
    dialog.resource?.use(context.metadata)
  }
}