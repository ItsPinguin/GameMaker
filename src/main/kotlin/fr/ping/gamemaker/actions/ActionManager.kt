package fr.ping.gamemaker.actions

import fr.ping.gamemaker.actions.models.Action
import fr.ping.gamemaker.GameMakerPlugin.Companion.actionExecutorRegistry
import fr.ping.gamemaker.actions.impl.CommandAction
import fr.ping.gamemaker.actions.impl.DialogAction
import fr.ping.gamemaker.actions.impl.GiveItemAction
import fr.ping.gamemaker.actions.impl.MessagePlayer

object ActionManager {
  fun executeAction(action: Action, context: Map<String, Any?> = mapOf()) =
    actionExecutorRegistry.listResources()
      .forEach { executor -> executor.execute(action, context) }

  init {
    actionExecutorRegistry.registerResource("message_player", MessagePlayer)
    actionExecutorRegistry.registerResource("dialog", DialogAction)
    actionExecutorRegistry.registerResource("give_item", GiveItemAction)
    actionExecutorRegistry.registerResource("command", CommandAction)
  }
}