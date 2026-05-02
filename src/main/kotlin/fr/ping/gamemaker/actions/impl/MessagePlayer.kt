package fr.ping.gamemaker.actions.impl

import fr.ping.gamemaker.GameMakerPlugin
import fr.ping.gamemaker.actions.ActionExecutor
import fr.ping.gamemaker.actions.models.Action
import org.bukkit.entity.Player

object MessagePlayer : ActionExecutor() {
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
}