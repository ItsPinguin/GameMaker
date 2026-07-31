package fr.itspinguin.gamemaker.actions.impl

import fr.itspinguin.gamemaker.actions.ActionContext
import fr.itspinguin.gamemaker.actions.ActionExecutor
import fr.itspinguin.gamemaker.actions.models.Action
import fr.itspinguin.gamemaker.dialog.Dialog
import fr.itspinguin.resourcemanager.ResourceManager

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