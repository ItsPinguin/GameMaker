package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.utils.resources.ResourceManager

object DialogAction : ActionExecutor() {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action !in listOf("dialog", "npc", "npc_dialog")) return
    val dialogId = action.data["dialog"] as? String ?: return
    @Suppress("DEPRECATION")
    val dialog = ResourceManager[dialogId, Dialog::class.java] ?: return
    dialog.resource?.use(context)
  }
}