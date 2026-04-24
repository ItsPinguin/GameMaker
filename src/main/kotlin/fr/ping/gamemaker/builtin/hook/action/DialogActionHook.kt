package fr.ping.gamemaker.builtin.hook.action

import fr.ping.gamemaker.addon.ActionExecutorHook
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.resource.Action
import fr.ping.utils.resources.ResourceManager

object DialogActionHook : ActionExecutorHook {
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