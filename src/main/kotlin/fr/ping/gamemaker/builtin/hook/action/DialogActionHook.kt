package fr.ping.gamemaker.builtin.hook.action

import fr.ping.gamemaker.GameMakerPlugin.Companion.gmk
import fr.ping.gamemaker.addon.ActionExecutorHook
import fr.ping.gamemaker.builtin.resource.dialog.Dialog
import fr.ping.gamemaker.resource.Action

object DialogActionHook : ActionExecutorHook {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action !in listOf("dialog", "npc", "npc_dialog")) return
    val dialogId = action.data["dialog"] as? String ?: return
    @Suppress("DEPRECATION")
    val dialog = gmk.useRegistry<Dialog>("dialogs")[dialogId] ?: return
    dialog.use(context)
  }

  override fun getId(): String {
    return "dialog"
  }

  override fun clean() {

  }
}