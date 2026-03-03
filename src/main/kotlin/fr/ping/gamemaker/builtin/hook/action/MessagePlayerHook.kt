package fr.ping.gamemaker.builtin.hook.action

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.addon.ActionExecutorHook
import fr.ping.gamemaker.resource.Action
import org.bukkit.entity.Player

object MessagePlayerHook : ActionExecutorHook {
  override fun execute(
    action: Action,
    context: Map<String, Any?>
  ) {
    if (action.action != "message_player") return
    (context["player"] as? Player?)?.sendMessage(action.data["message"] as? String ?: "no_message").let {
      if (it==null)
        GameMakerPlugin.getInstance().logger.info("Didn't work")
      else
        GameMakerPlugin.getInstance().logger.info("Worked")
    }
  }

  override fun getId(): String {
    return "message_player"
  }

  override fun clean() {
  }
}